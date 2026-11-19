package game

import basicstuff.Keys.shopsKey
import mydata.Currency
import mydata.ShopPage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import utils.editMeta

class ShopHeader(
    val name: String,
    val icon: ItemStack,
    val category: ShopPage
) {
    init {
        icon.editMeta {
            persistentDataContainer.set(shopsKey, PersistentDataType.STRING, name)
        }
    }
}

class ShopItem(
    val name: String,
    val icon: ItemStack,
    val category: ShopPage,
    val price: Int,
    val currency: Currency,
    val needsSpace: Boolean,
    val onClick: (Player) -> Unit
) {
    init {
        icon.itemMeta = icon.itemMeta!!.apply {
            persistentDataContainer.set(shopsKey, PersistentDataType.STRING, name)
            lore = when (currency) {
                Currency.FREE -> emptyList()
                Currency.GOLD -> listOf("§6⛃$price")
                Currency.STARS -> listOf("§3☆$price")
            } + (this.lore ?: emptyList())
        }
    }
}

fun buildShopMenu(page: ShopPage): Inventory {
    val inv = Bukkit.createInventory(null, 54, "Shop")

   mydata.shopHeaders.values.forEachIndexed { index, header ->inv.setItem(index+1, header.icon) }

    mydata.shopItems.values.filter{it.category == page}.forEachIndexed { index, item->
        inv.setItem(19 + index % 7 + index / 7 * 9, item.icon)
    }
    return inv
}


fun Player.hasRoom() = inventory.firstEmpty() != -1

fun grantByTeam(player: Player, blue: ItemStack, pink: ItemStack) {
    when (player.uniqueId) {
        in GameState.team1.members -> player.inventory.addItem(blue)
        in GameState.team2.members -> player.inventory.addItem(pink)
    }
}