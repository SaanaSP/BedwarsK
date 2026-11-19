package commands

import basicstuff.Keys.cheatsKey
import mydata.ShopPage
import mydata.shopMenus
import game.GameState
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import utils.arbItem
import utils.playSound
import java.util.UUID

object Cheats : CommandExecutor {

    val map = mutableMapOf<UUID, UUID>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (args.isEmpty()) {
            map[sender.uniqueId] = sender.uniqueId
        }
        else {
            val target = Bukkit.getPlayer(args[0]) ?: return true.also {sender.sendMessage("§Player §e${args[0]} §cnot found.")}
            map[sender.uniqueId] = target.uniqueId
        }
        sender.openInventory(CheatMenu)
        return true
    }
}

class MenuButton(
    val name: String,
    val icon: ItemStack,
    val onClick: (Player) -> Unit,
) {
    init {
        icon.itemMeta = icon.itemMeta!!.apply {
            persistentDataContainer.set(cheatsKey, PersistentDataType.STRING, name)
        }
    }
}

val cheatButtons = listOf(
    MenuButton("openShop", arbItem(Material.CHEST_MINECART, 1, "§bOpen Shop")) {player ->
        player.openInventory(shopMenus[ShopPage.BLOCKS]!!)
    },
    MenuButton("addGold", arbItem(Material.GOLD_NUGGET, 1, "§bAdd Gold", listOf("§r§7Give §6⛃100")).apply{
        itemMeta = itemMeta!!.apply{
            setMaxStackSize(99)
        }
        amount = 99
    }) {player ->
        GameState.playerStatMap[player.uniqueId]!!.gold += 100
        player.playSound(Sound.BLOCK_NOTE_BLOCK_PLING, 0.6f, 2f)
    },
    MenuButton("addGold1000", arbItem(Material.GOLD_BLOCK, 1, "§bAdd Gold", listOf("§r§7Give §6⛃1000")).apply{
        itemMeta = itemMeta!!.apply{
            setMaxStackSize(99)
        }
        amount = 99
    }) {player ->
        GameState.playerStatMap[player.uniqueId]!!.gold += 1000
        player.playSound(Sound.BLOCK_NOTE_BLOCK_PLING, 0.6f, 2f)
    },
    MenuButton("addStar", arbItem(Material.NETHER_STAR, 1, "§bAdd Star", listOf("§r§7Give §3☆1"))) {player ->
        val team = if (player.uniqueId in GameState.team1.members) GameState.team1 else GameState.team2
        team.stars++
        player.playSound(Sound.BLOCK_AMETHYST_CLUSTER_HIT, 0.6f, 2f)
    },
    MenuButton("addStars", arbItem(Material.NETHER_STAR, 10, "§bAdd Stars", listOf("§r§7Give §3☆10"))) {player ->
        val team = if (player.uniqueId in GameState.team1.members) GameState.team1 else GameState.team2
        team.stars += 10
        player.playSound(Sound.BLOCK_AMETHYST_CLUSTER_HIT, 0.6f, 2f)
    },
    MenuButton("revive", arbItem(Material.GOLDEN_APPLE, 1, "§bRevive")) {player ->
        TODO("Revive logic here")
        player.playSound(Sound.BLOCK_BEACON_POWER_SELECT, 0.6f, 1f)
    },
    MenuButton("heal", arbItem(Material.BLAZE_POWDER, 1, "§bHeal")) {player ->
        player.health = player.getAttribute(Attribute.MAX_HEALTH)!!.value
        player.playSound(Sound.BLOCK_BEACON_POWER_SELECT, 0.6f, 2f)
    },

).associateBy{it.name}


val CheatMenu = Bukkit.createInventory(null, 36, "Cheats").apply {
    cheatButtons.values.forEachIndexed { index, button ->
        setItem(10 + index % 7 + index / 7 * 9, button.icon)
    }
}