package commands

import game.hasRoom
import mydata.Items
import mydata.shopItems
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import utils.bad
import java.util.Locale
import java.util.Locale.getDefault

object Item : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return true//.also {(sender as? Player)?.bad("No item id provided")}
        if (sender !is Player) return false
        if (args[0].lowercase() !in Items.idMap.keys) return true.also {sender.bad("No such item exists")}
        if (!sender.hasRoom()) return true.also {sender.bad("No inventory space")}

        sender.inventory.addItem(Items[args[0]]!!.item)
        return true
    }
}