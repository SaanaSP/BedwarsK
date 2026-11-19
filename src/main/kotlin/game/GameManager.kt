package game

import mydata.PlayerData
import mydata.altars
import mydata.blueGenerator
import mydata.pinkGenerator
import mydata.world
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import utils.editMeta
import utils.forAllPlayers
import utils.scheduleTask
import kotlin.collections.component1

enum class GamePhase { NONE, TEAMS, ACTIVE }

enum class TeamTag {BLUE, RED, NONE}

fun numToTeamTag(num: Int): TeamTag {
    if (num == 1) { return TeamTag.BLUE}
    else if (num == 2) { return TeamTag.RED}
    else {throw IllegalArgumentException("No team found")}
}

// Reset
val beacons = setOf(
    Location(world, -70.0, 40.0, 40.0), // Pink left
    Location(world, -40.0, 40.0, 70.0), // Pink right
    Location(world, 70.0, 40.0, -40.0), // Blue left
    Location(world, 40.0, 40.0, -70.0) // Blue right
)
val goldBlocks = setOf(
    Location(world, -25.0, 50.0, -45.0), // Left
    Location(world, -45.0, 50.0, -25.0), // Right
    Location(world, -59.0, 50.0, -60.0), // Pile left
    Location(world, -60.0, 50.0, -59.0), // Pile right
    Location(world, -60.0, 50.0, -60.0), // Pile centre
    Location(world, -60.0, 50.0, -61.0), // Pile back left
    Location(world, -61.0, 50.0, -60.0), // Pile back right
    Location(world, -60.0, 51.0, -60.0), // Pile top\
    Location(world, -66.0, 47.0, -66.0) // Pile hidden
)

val tagsToClear = setOf("shopkeeper", "trap")

fun startGame() {
    world!!.entities.filter {it.scoreboardTags.any{it2 -> it2 in tagsToClear}}.forEach {it.remove()}

    GameState.team1.members.clear()
    GameState.team2.members.clear()

    GameState.gamePhase = GamePhase.TEAMS
    Bukkit.getOnlinePlayers().shuffled().forEachIndexed { index, value ->
        if (index % 2 == 0) GameState.team1.members.add(value.uniqueId) else GameState.team2.members.add(value.uniqueId)
        GameState.playerStatMap[value.uniqueId] = PlayerData(value.uniqueId)
    }

    resetMap()

    utils.broadcast("§aGame starting in 15 seconds. /teams to change teams")
    scheduleTask("teamSelectionEnd", 300L, 0L, func=::beginGame)
}

fun spawnShopkeeper(location: Location): Villager =
    (location.world!!.spawnEntity(location, EntityType.VILLAGER) as Villager).apply {
        isInvulnerable = true
        setAI(false)
        isPersistent = true
        profession = Villager.Profession.NONE
        addScoreboardTag("shopkeeper")
    }

fun beginGame() {
    GameState.gamePhase = GamePhase.ACTIVE
    for (id in GameState.team1.members) Bukkit.getPlayer(id)?.teleport(Location(world, 70.5, 40.0, -69.5, 44F, 0F))
    for (id in GameState.team2.members) Bukkit.getPlayer(id)?.teleport(Location(world, -69.5, 40.0, 70.5, -135F,0F))
    scheduleTask("goldIncrement", 0L, 20L, func=::addGold)
    scheduleTask("pollAltar", 0L, 2L, func=::pollAltar)
    GameState.playerStatMap.keys.forEach { initialiseScoreboard(it); updateScoreboard(it) }
    val blueShopkeeper = spawnShopkeeper(Location(world, 70.5, 40.0, -77.5, 0f, 0f))
    val redShopkeeper = spawnShopkeeper(Location(world, -69.5, 40.0, 78.5, 180f, 0f))
    forAllPlayers { player -> player.apply {
        gameMode = GameMode.SURVIVAL
        health = getAttribute(Attribute.MAX_HEALTH)!!.value
        inventory.apply{
            clear()
            setArmorContents(null)
            setItemInOffHand(null)
            setItemInMainHand(ItemStack(Material.WOODEN_SWORD).editMeta {
                isUnbreakable = true
            })
            activePotionEffects.forEach {player.removePotionEffect(it.type)}
            Attribute.values().forEach { attr -> //Love Claude 🥰
                getAttribute(attr)?.let { inst ->
                    inst.modifiers.toList().forEach { inst.removeModifier(it) }
                }
            }
        }
    }}
}

fun addGold() {
    GameState.playerStatMap.forEach { (id, stats) ->
        stats.gold++
        val loc = Bukkit.getPlayer(id)?.location?.toVector() ?: return@forEach
        if (blueGenerator.contains(loc) || pinkGenerator.contains(loc)) {
            stats.gold++
        }
    }
}


//FIX THISSSSSSSSSSSSSSSSS
fun pollAltar() {
    val balances = mutableListOf(0, 0) //negative = blue
    GameState.playerStatMap.forEach { (id, stats) ->
        val player = Bukkit.getPlayer(id)
        val loc = player?.location?.toVector() ?: return@forEach //continue. Fix Kotlin version\
        val team = if (id in GameState.team1.members) -1 else 1
        altars.forEachIndexed {i, area -> if (area.contains(loc)) { balances[i] += team
            //add sounds+
        } }
    }

    for (i in 0..1) {
        GameState.altarProgress[i] = (GameState.altarProgress[i] + balances[i]).coerceIn(-100, 100)
        if (GameState.altarProgress[i] <= -100) GameState.altars[i] = TeamTag.BLUE
        if (GameState.altarProgress[i] >= 100) GameState.altars[i] = TeamTag.RED
    }
}

fun resetMap() {
    for (loc in GameState.placedBlocks) {
        loc.block.type = Material.AIR
    }
    GameState.placedBlocks.clear()
    for (loc in beacons) loc.block.type = Material.BEACON
    for (loc in goldBlocks) loc.block.type = Material.GOLD_BLOCK
}