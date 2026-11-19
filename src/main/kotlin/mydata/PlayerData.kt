package mydata

import game.updateScoreboard
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.potion.PotionEffectType
import utils.getTeamById
import utils.playSound
import java.util.UUID

class PlayerData(
    val id: UUID,
    var kills: Int = 0,
    gold: Int = 0,
    snareLevel: Int = 0,
) {

    var gold: Int = gold
        set(value) {
            field = value
            updateScoreboard(id)
        }

    fun addGold(amount: Int) {
        gold += amount * getTeamById(id)?.upgrades["gold"]!!
    }

    var snareLevel: Int = snareLevel
        set(value) {
            if (field == value) return
            field = value

            val player = Bukkit.getPlayer(id) ?: return
            player.playSound(Sound.BLOCK_BAMBOO_BREAK, 0.3f, 0.5f)

            if (value == 0) {
                player.apply {
                    getAttribute(Attribute.JUMP_STRENGTH)?.removeModifier(nullAttribute)
                    getAttribute(Attribute.MOVEMENT_SPEED)?.removeModifier(nullAttribute)
                    removePotionEffect(PotionEffectType.BLINDNESS)
                    playSound(Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.3f, 1f)
                }
            }
        }
}