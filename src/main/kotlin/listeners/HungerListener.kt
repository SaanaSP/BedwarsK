package listeners

import game.GamePhase
import game.GameState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

object HungerListener : Listener {

    @EventHandler
    fun onBlockPlace(event: FoodLevelChangeEvent) {
        if (GameState.gamePhase == GamePhase.NONE) return
        event.entity.foodLevel = 20
        event.isCancelled = true
    }
}