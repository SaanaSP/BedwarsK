package listeners

import game.GameState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import utils.team
import utils.trifecta


object MoveListener : Listener {

    @EventHandler
    fun trapTest(event: PlayerMoveEvent) {
        if (event.player.trifecta) return

        val to = event.to ?: return
        val from = event.from

        if (from.blockX == to.blockX && from.blockZ == to.blockZ && from.blockY == to.blockY) return

        val trap = GameState.traps[to.block.location] ?: return
        if (event.player.team == trap.team) return
        trap.trigger(event.player)
    }
}