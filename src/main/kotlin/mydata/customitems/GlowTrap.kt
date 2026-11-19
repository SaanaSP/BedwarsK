package mydata.customitems

import mydata.BaseTrap
import mydata.CustomItem
import mydata.TrapType
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import utils.editMeta

object GlowTrap : BaseTrap(
    "trap_reveal",
    ItemStack(Material.SHROOMLIGHT, 1).editMeta {
        setDisplayName("§rGlow Trap")
        setMaxStackSize(1)
    }
) {
    override val cancels = true

    val type = TrapType.GLOW

    override fun func(event: BlockPlaceEvent) {
        super.place(event, TrapType.GLOW, listOf(
            Pair(EntityType.BLOCK_DISPLAY, {
                (this as BlockDisplay).block = Material.SHROOMLIGHT.createBlockData()
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