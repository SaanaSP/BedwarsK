package mydata.customitems

import mydata.BaseTrap
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

object WeakTrap : BaseTrap(
    "trap_slowness",
    ItemStack(Material.SUSPICIOUS_SAND, 1).editMeta {
        setDisplayName("§rWeak Trap")
        setMaxStackSize(1)
    }
) {
    override val cancels = true

    val type = TrapType.WEAK

    override fun func(event: BlockPlaceEvent) {
        super.place(event, TrapType.WEAK, listOf(
            Pair(EntityType.BLOCK_DISPLAY, {
                (this as BlockDisplay).block = Material.SUSPICIOUS_SAND.createBlockData()
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