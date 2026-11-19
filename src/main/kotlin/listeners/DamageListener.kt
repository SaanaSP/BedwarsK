package listeners

import basicstuff.Keys.itemsKey
import mydata.tntMap
import game.GamePhase
import game.GameState
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.entity.TNTPrimed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector
import utils.broadcast
import utils.editMeta
import utils.minus
import utils.plus
import utils.stats
import utils.team


object DamageListener : Listener {

    @EventHandler
    fun interaction(event: EntityDamageByEntityEvent) {
        if (GameState.gamePhase == GamePhase.NONE) return
        val interaction = event.entity
        if (interaction !is Interaction) return

        if (interaction in tntMap) {
            val player = event.damager as? Player ?: return
            val playerPos = player.eyeLocation.toVector()
            val interactionPos = interaction.location.toVector() + Vector(0.0, 0.5, 0.0)
            val tnt = tntMap.remove(interaction)!!
            tnt.velocity = (interactionPos - playerPos).normalize() + Vector(0.0, 0.3, 0.0)
            interaction.remove()
        }

    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (GameState.gamePhase == GamePhase.NONE) return

        val victim = event.entity
        if (victim !is Player || victim.uniqueId !in GameState.playerStatMap) return
        if (event is EntityDamageByEntityEvent && event.damager is TNTPrimed) {event.isCancelled = true; return}

        if (victim.stats!!.snareLevel > 0) victim.velocity = Vector(0.0, 0.0, 0.0)

        if (victim.health - event.damage <= 0) {
            event.isCancelled = true
            die(victim, event)
        }
    }
}

fun die(player: Player, event: EntityDamageEvent) {
    player.gameMode = GameMode.SPECTATOR
    dropGold(player)
    var deathMessage = when (event.cause) {
        EntityDamageEvent.DamageCause.FALL -> "§e${player.name}§f fell to their death"
        EntityDamageEvent.DamageCause.SUFFOCATION -> "§e${player.name}§f died in a wall somehow"
        EntityDamageEvent.DamageCause.DROWNING -> "§e${player.name}§f drowned"
        EntityDamageEvent.DamageCause.FIRE_TICK -> "§e${player.name}§f burned to death"
        EntityDamageEvent.DamageCause.VOID -> "§e${player.name}§f fell into the void"
        EntityDamageEvent.DamageCause.BLOCK_EXPLOSION -> "§e${player.name}§f blew up"
        else -> null
    }
    if (event is EntityDamageByEntityEvent) {
        when (val damager = event.damager) {
            is Player -> {
                GameState.playerStatMap[damager.uniqueId]!!.kills++
                val team = if (damager.uniqueId in GameState.team1.members) GameState.team1 else GameState.team2
                team.stars++
                deathMessage = "§e${player.name}§f was killed by §c${damager.name}"
                damager.world.playSound(damager.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f ,1f)
                player.world.spawnParticle(
                    Particle.BLOCK,
                    player.location.add(0.0, 1.5, 0.0),
                    20, 0.1, 0.1, 0.1,
                    Material.NETHER_WART_BLOCK.createBlockData()
                )
            }
        }
    }
    deathMessage?.let {broadcast("§c☠ $it")} ?: broadcast("§c☠ §e${player.name} died")
}

fun dropGold(player: Player) {
    val world = player.world
    val teamCode = if (player.team == GameState.team1) "coin_1" else "coin_2"
    for (i in 1..5) {
        world.dropItemNaturally(player.location, ItemStack(Material.GOLD_NUGGET).apply{
            editMeta {
                setMaxStackSize(i)
                persistentDataContainer.set(itemsKey, PersistentDataType.STRING, teamCode)
            }
        })
    }
}