package listeners

import basicstuff.Keys
import mydata.tntMap
import mydata.items
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Interaction
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import utils.isActive
import utils.plus

object ClickListener : Listener {

    @EventHandler
    fun customItemListener(event: PlayerInteractEvent) {
        val player = event.player
        if (!player.isActive) return

        event.isCancelled = true

        val tag = player.inventory.itemInMainHand.itemMeta?.persistentDataContainer?.get(Keys.itemsKey, PersistentDataType.STRING) ?: return
        items[tag]?.invoke(player)
    }

    @EventHandler
    fun genericListener(event: PlayerInteractEvent) {
        val player = event.player
        if (!player.isActive) return

        val block = event.clickedBlock

        if (event.action == Action.LEFT_CLICK_BLOCK && block!!.type == Material.TNT && !player.isSneaking) {
            block.type = Material.AIR
            val pos = block.location + Location(null, 0.5, 0.0, 0.5)
            val tnt = pos.world!!.spawnEntity(pos, EntityType.TNT)
            val hitbox = ((pos.world!!.spawnEntity(pos, EntityType.INTERACTION)) as Interaction).apply{
                interactionWidth = 1.5f
                interactionHeight = 1.5f
            }
            tntMap[hitbox] = tnt
        }
    }

    @EventHandler
    fun interactListener(event: PlayerInteractEvent) {
        val player = event.player
        if (!player.isActive) return

        //customItemListener(event)
        //genericListener(event)
    }
}