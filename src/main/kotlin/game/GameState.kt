package game

import mydata.PlayerData
import mydata.Team
import mydata.Trap
import org.bukkit.Location
import org.bukkit.scoreboard.Scoreboard
import java.util.UUID

object GameState {
    var gamePhase = GamePhase.NONE
    val playerStatMap = mutableMapOf<UUID, PlayerData>()
    val scoreboardMap = mutableMapOf<UUID, Scoreboard>()
    val team1 = Team()
    val team2 = Team()
    val teams = mapOf(TeamTag.BLUE to team1, TeamTag.RED to team2)
    val placedBlocks = mutableSetOf<Location>()
    val altars = mutableListOf(TeamTag.NONE, TeamTag.NONE)
    val altarProgress = mutableListOf(0, 0)
    val traps = mutableMapOf<Location, Trap>()
}
