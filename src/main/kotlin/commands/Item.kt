package commands

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
        if (args.isEmpty()) return false.also {(sender as? Player)?.bad("No item id provided")}
        if (args[0].lowercase() !in shopItems.keys) return false.also {(sender as? Player)?.bad("No such item exists")}

        return true
    }
}