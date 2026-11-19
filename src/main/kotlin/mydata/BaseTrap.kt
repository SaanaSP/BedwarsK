package mydata

import basicstuff.plugin
import game.GameState
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import utils.bad
import utils.isFullCube
import utils.team
import utils.teamNum

abstract class BaseTrap(
    name: String,
    item: ItemStack,
) : Placeable, CustomItem(
    name,
    item
) {
    fun place(event: BlockPlaceEvent, type: TrapType, displays: List<Pair<EntityType, Display.() -> Unit>>) {

        val player = event.player

        if (!quadMap[player.teamNum!!]!!.contains(event.block.location.toVector())) return player.bad("You can only place traps in your quadrant")
        if (!event.block.getRelative(BlockFace.DOWN).isFullCube) return player.bad("Traps need a full block")
        if (player.team!!.trapCount >= 10) return player.bad("Your team has reached the max number of traps")

        val visuals = displays.map {(world.spawnEntity(event.block.location, it.first) as Display). apply{
            it.second(this)
            addScoreboardTag("trap")
        } }

        GameState.traps[event.block.location] = Trap(
            player.team!!,
            event.block.location,
            type,
            visuals
        )

        player.team!!.trapCount++

        val targetTeam = GameState.teams.values.first {it != player.team}
        targetTeam.members.forEach { visuals.forEach {it2 -> Bukkit.getPlayer(it)?.hideEntity(plugin, it2)} }
    }
}