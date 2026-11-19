package listeners

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.block.BlockSpreadEvent


object GoldBlockListener : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.block.type != Material.GOLD_BLOCK || game.GameState.gamePhase == game.GamePhase.NONE) return

        val player = event.player
        if (player.uniqueId !in game.GameState.playerStatMap) {event.isCancelled = true; return}

        game.GameState.playerStatMap[player.uniqueId]!!.addGold(200)
        event.isDropItems = false
    }
}