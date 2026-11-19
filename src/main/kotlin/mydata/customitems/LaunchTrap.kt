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

object LaunchTrap : BaseTrap(
    "trap_launch",
    ItemStack(Material.PISTON, 1).editMeta {
        setDisplayName("§rLaunch Trap")
        setMaxStackSize(1)
    }
) {
    override val cancels = true

    val type = TrapType.LAUNCH

    override fun func(event: BlockPlaceEvent) {
        super.place(event, type, listOf(
            Pair(EntityType.BLOCK_DISPLAY, {
                (this as BlockDisplay).block = (Material.PISTON.createBlockData() as Directional).apply{facing=BlockFace.UP}
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