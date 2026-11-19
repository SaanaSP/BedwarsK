package mydata

import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent

interface Placeable { //maybe do this later for traps etc.

    val cancels: Boolean
    fun func(event: BlockPlaceEvent): Unit
}