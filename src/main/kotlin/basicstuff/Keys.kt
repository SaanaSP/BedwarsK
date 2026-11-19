package basicstuff

import org.bukkit.NamespacedKey

object Keys {
    val shopsKey = NamespacedKey(plugin, "SHOP_ID")
    val itemsKey = NamespacedKey(plugin, "ITEM_ID")
    val cheatsKey = NamespacedKey(plugin, "CHEATS_ID")

    val nullAttributeKey = NamespacedKey(plugin, "NULL_ATTR")
}