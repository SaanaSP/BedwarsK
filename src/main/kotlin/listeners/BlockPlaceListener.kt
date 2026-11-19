package listeners

import basicstuff.Keys.itemsKey
import basicstuff.plugin
import mydata.world
import game.GamePhase
import game.GameState
import mydata.Trap
import mydata.quadMap
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import utils.team
import org.joml.Vector3f;
import utils.bad
import utils.isFullCube
import utils.teamNum
import utils.trifecta

object BlockPlaceListener : Listener {

    @EventHandler
    fun trapListener(event: BlockPlaceEvent) {
        val player = event.player
        if (player.trifecta) return

        val id = event.itemInHand.itemMeta?.persistentDataContainer!!.get(itemsKey, PersistentDataType.STRING) ?: return
        if (!id.contains("trap")) return

        event.isCancelled = true

        if (!quadMap[player.teamNum!!]!!.contains(event.block.location.toVector())) return player.bad("You can only place traps in your quadrant")
        //if (!world.getBlockAt(event.block.location - Location(null, 0.0, 1.0, 0.0)).isFullCube) return
        if (!event.block.getRelative(BlockFace.DOWN).isFullCube) return player.bad("Traps need a full block") 
        if (player.team!!.trapCount >= 10) return player.bad("Your team has reached the max number of traps")

        player.inventory.setItemInMainHand(null)

        if (GameState.traps.keys.contains(event.block.location)) return

        val material = when (id) {
            "trap_slowness" -> Material.SUSPICIOUS_SAND
            "trap_launch" -> Material.PISTON
            "trap_snare" -> Material.TARGET
            "trap_reveal" -> Material.SHROOMLIGHT
            else -> return
        }

        val visual = (world.spawnEntity(event.block.location, EntityType.BLOCK_DISPLAY) as BlockDisplay).apply {
            block = if (material == Material.PISTON) (material.createBlockData() as Directional).apply{facing=BlockFace.UP} else material.createBlockData()
            transformation = Transformation(
                Vector3f(0.1f, -0.75f, 0.1f),
                Quaternionf(),
                Vector3f(0.8f, 0.8f, 0.8f),

                Quaternionf()
            )
            isVisibleByDefault = true
            addScoreboardTag("trap")
        }

        GameState.traps[event.block.location] = Trap(
            player.team!!,
            event.block.location,
            id,
            visual
        )

        player.team!!.trapCount++

        val targetTeam = GameState.teams.values.first {it != player.team}
        targetTeam.members.forEach { Bukkit.getPlayer(it)?.hideEntity(plugin, visual) }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (GameState.gamePhase == GamePhase.NONE) return
        if (event.player.gameMode == GameMode.CREATIVE) return
        GameState.placedBlocks.add(event.block.location)
    }
}