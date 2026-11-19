package commands

import game.GameState.gamePhase
import game.TeamTag
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta


class Teams : CommandExecutor {

    //so cute of me
    companion object {
        var gui = Bukkit.createInventory(null, 18, "Teams")

        fun buildgui() {
            gui.clear()
            for (j in 0..1) {
                var i = 0
                for (id in game.GameState.teams[TeamTag.entries[j]]!!.members) {
                    val head = ItemStack(Material.PLAYER_HEAD, 1)
                    val meta = head.itemMeta as SkullMeta
                    val name: String = Bukkit.getOfflinePlayer(id).name ?: "Unknown"
                    meta!!.setDisplayName((if (j == 0) "§b" else "§c") + name)
                    meta.ownerProfile = Bukkit.createPlayerProfile(id)
                    head.itemMeta = meta
                    gui.setItem(i + j * 9, head)
                    i++
                }
            }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player && gamePhase == game.GamePhase.TEAMS) {
            buildgui()
            sender.openInventory(gui)
        }
        return true
    }
}