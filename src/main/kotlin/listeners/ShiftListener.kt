package listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import utils.stats
import utils.trifecta


object ShiftListener : Listener {

    @EventHandler
    fun coinPickup(event: PlayerToggleSneakEvent) {
        val player = event.player
        if (player.trifecta) return

        if (event.isSneaking) {
            player.stats!!.snareLevel = (player.stats!!.snareLevel - 1).coerceAtLeast(0)
        }
    }
}