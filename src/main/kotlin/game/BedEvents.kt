package game

import org.bukkit.Sound
import utils.broadcast
import utils.forAllPlayers

fun broadcastBedBreak(team: Int) {
    forAllPlayers { player -> player.playSound(player.location, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1.5f) }
    broadcast("§e-------------------------------")
    broadcast("")
    if (team == 1) broadcast("§b   Blue Bed §fhas been broken") else broadcast("§c   Pink Bed §fhas been broken")
    broadcast("")
    broadcast("§e-------------------------------")
}