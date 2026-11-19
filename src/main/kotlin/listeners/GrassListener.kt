package listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.block.BlockSpreadEvent


object GrassListener : Listener {

    @EventHandler
    fun onBlockSpread(event: BlockSpreadEvent) {
        if (event.source.type == Material.GRASS_BLOCK || event.source.type == Material.MYCELIUM) event.isCancelled = true
    }

    @EventHandler
    fun onBlockFade(event: BlockFadeEvent) {
        if (event.block.type == Material.GRASS_BLOCK || event.block.type == Material.MYCELIUM) event.isCancelled = true
    }
}