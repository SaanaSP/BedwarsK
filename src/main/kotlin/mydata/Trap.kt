package mydata

import basicstuff.plugin
import game.GameState
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import utils.TermColour
import utils.minus
import utils.playSound
import utils.scheduleTask
import utils.teamNum

class Trap(
    val team: Team,
    val location: Location,
    val type: String,
    val visual: BlockDisplay
) {
    fun trigger(player: Player) {
        when (type) {
            "trap_slowness" -> {
                player.apply {
                    addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 60, 5))
                    getAttribute(Attribute.JUMP_STRENGTH)!!.apply {
                        removeModifier(nullAttribute)
                        addModifier(nullAttribute)
                    }
                }
                player.playSound(Sound.ENTITY_WITHER_SKELETON_DEATH, 0.3f, 2f)
                scheduleTask("remove_noJump ${player.uniqueId}", 60L, 0L) {
                    Bukkit.getPlayer(player.uniqueId)?.player?.getAttribute(Attribute.JUMP_STRENGTH)
                        ?.removeModifier(nullAttribute)
                }
            }
            "trap_launch" -> {
                val delta = player.location.toVector() - when (player.teamNum) {
                    1 -> Vector(-69.5, 40.0, 70.5)
                    2 -> Vector(70.5, 40.0, -69.5)
                    else -> return
                }
                player.velocity = delta.apply{y=0.0}.normalize().multiply(2).apply{y=1.5}
                player.playSound(Sound.BLOCK_PISTON_EXTEND, 1.0f, 1.25f)
            }
            "trap_snare" -> {
                player.apply {
                    addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, -1, 0))
                    getAttribute(Attribute.JUMP_STRENGTH)!!.apply {
                        removeModifier(nullAttribute)
                        addModifier(nullAttribute)
                    }
                    getAttribute(Attribute.MOVEMENT_SPEED)!!.apply {
                        removeModifier(nullAttribute)
                        addModifier(nullAttribute)
                    }
                    spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§cSpam shift to break out"))
                    playSound(Sound.ENTITY_WITHER_BREAK_BLOCK, 0.3f, 1.5f)
                }
                GameState.playerStatMap[player.uniqueId]!!.snareLevel = 15
            }
            "trap_reveal" -> {
                player.removePotionEffect(PotionEffectType.INVISIBILITY)
                player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 400, 0))
                val ids = team.members
                scheduleTask("trap_alarm", 0L, 2L, 10) {
                    ids.mapNotNull { Bukkit.getPlayer(it) }.forEach { it2 ->
                        it2.playSound(Sound.BLOCK_NOTE_BLOCK_BELL, 0.3f, 0.5f)
                    }
                }
            }
        }
        disarm()
    }

    fun disarm() {
        val key = GameState.traps.entries.firstOrNull {it.value == this}?.key ?: return plugin.logger.info("${TermColour.RED}Stray trap")
        GameState.traps.remove(key)
        visual.remove()
        team.trapCount--
    }
}