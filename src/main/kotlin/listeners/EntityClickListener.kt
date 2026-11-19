package listeners

import mydata.ShopPage
import mydata.shopMenus
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

object EntityClickListener : Listener {

    fun shopkeeper (event: PlayerInteractEntityEvent) {
        event.isCancelled = true
        event.player.openInventory(shopMenus[ShopPage.BLOCKS]!!)
    }

    @EventHandler
    fun onEntityClick(event: PlayerInteractEntityEvent) {
        if (event.rightClicked is Villager) shopkeeper(event)
    }
}