package mydata.customitems

import basicstuff.Keys
import game.GameState
import mydata.CustomItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import utils.decrementItem
import utils.editMeta

object Platform : CustomItem(
    "platform",
    ItemStack(Material.SNOW, 1).editMeta {
        setDisplayName("§rPlatform")
    }
) {
    override fun func(player: Player) {
        for (i in -1..1) inner@ for (j in -1..1) {
            val block = player.world.getBlockAt(player.location.blockX+i, player.location.blockY-2, player.location.blockZ+j)
            if (!block.type.isAir) continue@inner
            block.type = Material.WHITE_WOOL
            GameState.placedBlocks.add(block.location)
        }
        player.world.playSound(player.location,Sound.BLOCK_WOOL_PLACE, 2f, 1f)
        decrementItem(player)
    }
}