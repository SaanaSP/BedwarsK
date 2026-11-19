package mydata

import java.util.UUID

class Team {
    val members = mutableListOf<UUID>()
    var stars = 0
    var bedAlive = true
    val upgrades = mutableMapOf(
        "gold" to 1,
        "haste" to 0,
        "healing" to 0
    )
    var trapCount = 0
}