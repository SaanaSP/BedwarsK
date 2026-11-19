package mydata

import basicstuff.Keys.nullAttributeKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup

val nullAttribute = AttributeModifier(
    nullAttributeKey,
    -1.0,
    AttributeModifier.Operation.MULTIPLY_SCALAR_1,
    EquipmentSlotGroup.ANY
)