package mydata

import org.bukkit.entity.Player

interface Consumable {
    fun func(player: Player): Unit
}