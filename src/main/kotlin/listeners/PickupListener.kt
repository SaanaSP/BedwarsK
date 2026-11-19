package listeners

import basicstuff.Keys
import game.GamePhase
import game.GameState
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.persistence.PersistentDataType
import utils.isActive
import utils.team


object PickupListener : Listener {

    @EventHandler
    fun coinPickup  (event: EntityPickupItemEvent) {
        if (GameState.gamePhase == GamePhase.NONE) return
        val player = event.entity as? Player ?: return

        if (!player.isActive) return
        val stats = GameState.playerStatMap[player.uniqueId]

        if (event.item.itemStack.type != Material.GOLD_NUGGET) return

        val code = event.item.itemStack.itemMeta!!.persistentDataContainer.get(Keys.itemsKey, PersistentDataType.STRING)
        val target = when (player.team) {
            GameState.team1 -> "coin_2"
            GameState.team2 -> "coin_1"
            else -> return
        }
        if (code==target) stats!!.addGold(80)
        player.playSound(player.location, Sound.ENTITY_ITEM_PICKUP, 0.7f, 2f)
        event.isCancelled = true
        event.item.remove()
    }
}