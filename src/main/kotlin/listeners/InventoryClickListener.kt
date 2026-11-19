package listeners

import basicstuff.Keys
import commands.Cheats.map
import mydata.Currency
import game.GamePhase
import game.GameState
import game.hasRoom
import mydata.shopHeaders
import mydata.shopItems
import mydata.shopMenus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.persistence.PersistentDataType

//fuck it just rework the entire teams thing
fun teamMenu(event: InventoryClickEvent) {
    event.isCancelled = true
    val item = event.currentItem
    if (item?.type != Material.PLAYER_HEAD) return
    val uuid = Bukkit.getPlayerExact(ChatColor.stripColor(item.itemMeta?.displayName) ?: return)?.uniqueId ?: return //maybe change to skull meta
    if (game.GameState.team1.members.remove(uuid)) {
        game.GameState.team2.members.add(uuid)
    } else if (game.GameState.team2.members.remove(uuid)) {
        game.GameState.team1.members.add(uuid)
    }
    commands.Teams.buildgui()
}

fun shop(event: InventoryClickEvent) {
    event.isCancelled = true
    val player = event.whoClicked as Player

    val tag = event.currentItem?.itemMeta?.persistentDataContainer?.get(Keys.shopsKey, PersistentDataType.STRING) ?: return

    if (tag in shopItems.keys) purchase(event, player, tag)
    if (tag in shopHeaders.keys) player.openInventory(shopMenus[shopHeaders[tag]!!.category]!!)


}

fun purchase(event: InventoryClickEvent, player: Player, tag: String) {
    val item = shopItems[tag]!!
    if (item.needsSpace && !player.hasRoom()) return
    if (player.uniqueId !in game.GameState.playerStatMap) return

    if (item.currency == Currency.GOLD) {
        val playerstats = game.GameState.playerStatMap[player.uniqueId]!!

        if (playerstats.gold < item.price) { player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 0.6f, 1f); return }

        item.onClick(player)
        playerstats.gold -= item.price
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.6f, 2f)
    } else if (item.currency == Currency.STARS) {
        val team = if (player.uniqueId in game.GameState.team1.members) game.GameState.team1 else game.GameState.team2

        if (team.stars < item.price) { player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 0.6f, 1f); return }

        item.onClick(player)
        team.stars -= item.price
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.6f, 2f)
    } else if (item.currency == Currency.FREE) {
        item.onClick(player)
    }

}

fun cheats(event: InventoryClickEvent) {
    event.isCancelled = true
    if (GameState.gamePhase == GamePhase.NONE) return
    val player = event.whoClicked as Player
    val button = event.currentItem ?: return
    val tag = button.itemMeta!!.persistentDataContainer.get(Keys.cheatsKey, PersistentDataType.STRING) ?: return player.sendMessage("§cNo tag")

    val cheat = commands.cheatButtons[tag] ?: return player.sendMessage("§cCheat $tag not found")
    val target = Bukkit.getPlayer(map[player.uniqueId]!!) ?: return player.sendMessage("§cPlayer not found")

    cheat.onClick(target)
}

object InventoryClickListener : Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        when (event.view.title) {
            "Teams" -> teamMenu(event)
            "Shop" -> shop(event)
            "Cheats" -> cheats(event)
        }
    }
}