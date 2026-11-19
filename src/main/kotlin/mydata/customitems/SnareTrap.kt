package mydata.customitems

import mydata.BaseTrap
import mydata.CustomItem
import mydata.TrapType
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import utils.editMeta

object SnareTrap : BaseTrap(
    "trap_snare",
    ItemStack(Material.TARGET, 1).editMeta {
        setDisplayName("§rSnare Trap")
        setMaxStackSize(1)
    }
) {
    override val cancels = true

    val type = TrapType.SNARE

    override fun func(event: BlockPlaceEvent) {
        super.place(event, TrapType.SNARE,  listOf(
            Pair(EntityType.BLOCK_DISPLAY, {
                (this as BlockDisplay).block = Material.TARGET.createBlockData()
                transformation = Transformation(
                    Vector3f(0.1f, -0.75f, 0.1f),
                    Quaternionf(),
                    Vector3f(0.8f, 0.8f, 0.8f),
                    Quaternionf()
                )
            })
        ))
    }
}