package game

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.UUID

fun initialiseScoreboard(id: UUID) {
    val scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard
    val obj = scoreboard.registerNewObjective("bw", "dummy", "  §e§lGame  ")
    obj.displaySlot = DisplaySlot.SIDEBAR
    val lineEntries = listOf("§a", "§b", "§c", "§d", "§e", "§f", "§0")
    lineEntries.forEachIndexed { i, entry ->
        val team = scoreboard.registerNewTeam("line$i") //when you give a team a prefix, it writes it.
        team.addEntry(entry)
        obj.getScore(entry).score = lineEntries.size - i
    }
    GameState.scoreboardMap[id] = scoreboard
}

fun updateScoreboard(id: UUID) {
    val scoreboard = GameState.scoreboardMap.getValue(id)
    val data = GameState.playerStatMap[id]
    val team = if (id in GameState.team1.members) GameState.team1 else GameState.team2

    scoreboard.getTeam("line0")!!.prefix = ""
    scoreboard.getTeam("line1")!!.prefix = "§6⛃ ${data!!.gold}"
    scoreboard.getTeam("line2")!!.prefix = "§3☆ ${team.stars}"
    scoreboard.getTeam("line3")!!.prefix = ""
    scoreboard.getTeam("line4")!!.prefix = "§bBlue: §a✔"
    scoreboard.getTeam("line5")!!.prefix = "§cPink: §a✔"
    scoreboard.getTeam("line6")!!.prefix = ""
    Bukkit.getPlayer(id)?.scoreboard = scoreboard
}