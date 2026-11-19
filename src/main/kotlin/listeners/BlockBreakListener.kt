package listeners

import mydata.blueBed
import mydata.pinkBed
import mydata.tntMap
import game.GameState
import game.broadcastBedBreak
import game.numToTeamTag
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityExplodeEvent
import utils.isActive
import utils.team

object BlockBreakListener : Listener {


    //maybe rework these into eventhandler
    fun gold(event: BlockBreakEvent) {
        if (!event.player.isActive) return
        GameState.playerStatMap[event.player.uniqueId]!!.addGold(200)
        event.player.playSound(event.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.65f, 1.5f)
        event.isDropItems = false
    }

    fun bedBreak(event: BlockBreakEvent) {
        val player = event.player
        if (!player.isActive) return
        val number = when (event.block.location) {
            in blueBed if player.team == GameState.team2 -> 1
            in pinkBed if player.team == GameState.team1 -> 2
            else -> {
                player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 0.3f, 0.5f)
                event.isCancelled = true
                return
            }
        }
        broadcastBedBreak(number)
        GameState.teams[numToTeamTag(number)]!!.bedAlive = false
        GameState.playerStatMap[player.uniqueId]!!.addGold(750)
        player.team!!.members.forEach { GameState.playerStatMap[it]!!.addGold(250) }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (GameState.gamePhase == game.GamePhase.NONE) return
        if (event.player.gameMode == GameMode.CREATIVE) return

        val block = event.block
        if (block.type == Material.GOLD_BLOCK && block.location in game.goldBlocks) {
            gold(event)
        } else if (Tag.BEDS.isTagged(block.type)) {
            bedBreak(event)
        } else {
            if (block.location !in GameState.placedBlocks) {event.isCancelled = true; return}
            //GameState.placedBlocks.remove(block.location)
        }
    }

    @EventHandler
    fun onBlockExplode(event: EntityExplodeEvent) {
        event.blockList().removeAll {it.location !in GameState.placedBlocks}
        event.blockList().forEach {GameState.placedBlocks.remove(it.location)}
        val key = tntMap.entries.firstOrNull {it.value == event.entity}?.key
        tntMap.remove(key)
        key?.remove()
    }

    @EventHandler
    fun disarmTraps(event: BlockBreakEvent) {
        val trap = GameState.traps[event.block.getRelative(BlockFace.UP).location] ?: return
        if (event.block.location !in GameState.placedBlocks && trap.team != event.player.team) return
        trap.disarm()
    }
}