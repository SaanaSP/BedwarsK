package commands

import basicstuff.schedule
import game.GameState.gamePhase
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CmdStart : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (gamePhase) {
            game.GamePhase.ACTIVE -> {
                if (sender is Player) sender.sendMessage("§cUnable to start. Game already running.")
                return true
            }
            game.GamePhase.TEAMS -> {
                schedule["teamSelectionEnd"]!!.cancel()
                game.beginGame()
            }
            game.GamePhase.NONE -> game.startGame()
        }
        return true
    }
}