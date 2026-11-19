package mydata

import basicstuff.Keys
import game.GameState
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import utils.decrementItem
import utils.editMeta

val items = mapOf(
    "platform" to {player: Player ->
        for (i in -1..1) inner@ for (j in -1..1) {
            val block = player.world.getBlockAt(player.location.blockX+i, player.location.blockY-2, player.location.blockZ+j)
            if (!block.type.isAir) continue@inner
            block.type = Material.WHITE_WOOL
            GameState.placedBlocks.add(block.location)
        }
        player.world.playSound(player.location,Sound.BLOCK_WOOL_PLACE, 2f, 1f)
        decrementItem(player)
    },
    "boost" to {player: Player ->
        player.velocity = player.location.direction.multiply(1.5).apply{y=1.0}
        player.world.playSound(player.location,Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)
        decrementItem(player)
    },
    "invis_cloak" to {player: Player -> //add cancel equip
        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 400, 0))
        player.playSound(player.location,Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.7f, 1f)
    }
)

object Items {

    val platform = mydata.customitems.Platform

    val boost = mydata.customitems.RocketBoost

    val invis_cloak = mydata.customitems.InvisibilityCloak
}