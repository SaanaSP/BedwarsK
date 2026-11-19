package mydata

import org.bukkit.Location
import org.bukkit.util.BoundingBox

val blueGenerator = BoundingBox(77.0, 42.0, -76.0, 74.0, 39.0, -73.0)
val pinkGenerator = BoundingBox(-76.0, 42.0, 77.0, -73.0, 39.0, 74.0)

val altars = listOf(
    BoundingBox(49.0, 50.0, 22.0, 42.0, 52.0, 29.0),
    BoundingBox(29.0, 50.0, 42.0, 42.0, 52.0, 22.0)
)

val blueBed = setOf(Location(world, 66.0, 40.0, -66.0), Location(world, 66.0, 40.0, -67.0))
val pinkBed = setOf(Location(world, -66.0, 40.0, 66.0), Location(world, -66.0, 40.0, 67.0))

val blueQuad = BoundingBox(1.5, 60.0, -0.5, 81.5, 0.0, -80.5)
val pinkQuad = BoundingBox(-0.5, 60.0, 1.5, -80.5, 0.0, 81.5)
val quadMap = mapOf(1 to blueQuad, 2 to pinkQuad)

val fullCube = BoundingBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)