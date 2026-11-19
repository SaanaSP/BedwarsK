package mydata.customitems

import basicstuff.Keys
import game.GameState
import mydata.Consumable
import mydata.CustomItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import utils.decrementItem
import utils.editMeta

object RocketBoost : Consumable, CustomItem(
    "boost",
    ItemStack(Material.FIREWORK_ROCKET, 1).editMeta {
        setDisplayName("§rRocket Boost")
    }
) {
    override fun func(player: Player) {
        player.velocity = player.location.direction.multiply(1.5).apply{y=1.0}
        player.world.playSound(player.location,Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)
        decrementItem(player)
    }
}