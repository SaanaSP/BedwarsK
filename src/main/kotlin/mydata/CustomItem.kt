package mydata

import basicstuff.Keys.itemsKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import utils.editMeta

abstract class CustomItem(
    val name: String,
    item: ItemStack,
) {
    init {
        item.editMeta {
            persistentDataContainer.set(itemsKey, PersistentDataType.STRING, name)
        }
    }

    val item: ItemStack = item
    get() = field.clone()
}
