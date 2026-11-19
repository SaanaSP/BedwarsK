package mydata.customitems

import basicstuff.Keys
import game.GameState
import mydata.Consumable
import mydata.CustomItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import utils.decrementItem
import utils.editMeta

object InvisibilityCloak : Consumable, CustomItem(
    "invis_cloak",
    ItemStack(Material.CHAINMAIL_CHESTPLATE, 1).editMeta {
        setDisplayName("§rInvisibility Cloak")
        setEnchantmentGlintOverride(true)
        lore = listOf("§r§8Grants invisibility for", "§r§820 seconds")
    }
) {
    override fun func(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 400, 0))
        player.playSound(player.location,Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.7f, 1f)
    }
}