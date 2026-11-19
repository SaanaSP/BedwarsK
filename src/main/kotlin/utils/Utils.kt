package utils

import basicstuff.plugin
import basicstuff.schedule
import mydata.nullAttribute
import game.GamePhase
import game.GameState
import mydata.Team
import mydata.fullCube
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.io.File
import java.net.URL
import java.util.Base64
import java.util.UUID

fun broadcast(text: String) {
    for (i in Bukkit.getOnlinePlayers()) i.sendMessage(text)
}

fun scheduleTask(key: String, delay: Long = 0, period: Long = 0, times: Int = 0, func: () -> Unit): BukkitRunnable {
    val task = object : BukkitRunnable() {
        var runs = 0
        override fun run() {
            func()
            runs++
            if (runs >= times && times > 0) return cancel()
        }
        override fun cancel() {
            super.cancel()
            schedule.remove(key, this)
        }
    }
    schedule.put(key, task)?.cancel()
    when {
        period > 0 -> task.runTaskTimer(plugin, delay.coerceAtLeast(0), period)
        delay > 0 -> task.runTaskLater(plugin, delay)
        else -> task.runTask(plugin)
    }
    return task
}

fun loadWorldIfExists(name: String): World? {
    val worldFolder = File(Bukkit.getServer().worldContainer, name)

    if (!worldFolder.exists()) {
        Bukkit.getLogger().warning("World folder \"$name\" not found! Skipping load.")
        return null
    }

    Bukkit.getLogger().info("Loading world: $name")
    val world = WorldCreator(name).createWorld()

    if (world == null) {
        Bukkit.getLogger().warning("Failed to load world: $name")
    }
    return world
}

/*inline fun <reified T>arbItem(entry: T, amount: Int, name: String): ItemStack {
    when (T::class) {
        Material::class -> return ItemStack(entry as Material, amount).apply {
            itemMeta = itemMeta!!.apply {
                setDisplayName(name)
            }
        }
        ItemStack::class -> return (entry as ItemStack).apply {
            itemMeta = itemMeta!!.apply {
                setDisplayName(name)
            }
        }
        else -> throw IllegalArgumentException("Miau at arbitem")
    }
}*/

// Here for the memories
fun arbItem(item: ItemStack, name: String, loree: List<String> = emptyList(), pdkey: NamespacedKey? = null, pdtag: String? = null) =
    item.editMeta {
        setDisplayName(name)
        lore = loree
        if (pdkey != null && pdtag != null) {
            persistentDataContainer.set(pdkey, PersistentDataType.STRING, pdtag)
        }
    }


fun arbItem(material: Material, amount: Int, name: String, loree: List<String> = emptyList(), pdkey: NamespacedKey? = null, pdtag: String? = null) =
    arbItem(ItemStack(material, amount), name, loree, pdkey, pdtag)

//Ty Claude
fun makeHead(texture: String): ItemStack {
    val json = String(Base64.getDecoder().decode(texture))
    val url = json.substringAfter("\"url\":\"").substringBefore("\"")

    val profile = Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(texture.toByteArray()), "head")
    val textures = profile.textures
    textures.skin = URL(url)
    profile.setTextures(textures)

    return ItemStack(Material.PLAYER_HEAD).editMeta <SkullMeta> {
        ownerProfile = profile
    }
}

fun decrementItem(player: Player) {
    val item = player.inventory.itemInMainHand
    if (item.type == Material.AIR) return
    if (item.amount > 0) item.amount -= 1
    else player.inventory.setItemInMainHand(null)
}

val Player.team: Team?
    get() = getTeamById(this.uniqueId)

val Player.teamNum: Int?
    get() {return when (this.team) {
        GameState.team1 -> 1
        GameState.team2 -> 2
        else -> null
    }}

val Player.isActive: Boolean
    get() = this.uniqueId in GameState.playerStatMap && GameState.gamePhase == GamePhase.ACTIVE

val Player.trifecta: Boolean
    get() = GameState.gamePhase == GamePhase.NONE || this.gameMode == GameMode.CREATIVE || !this.isActive

fun Player.bad(message: String) {
    playSound(Sound.ENTITY_PLAYER_TELEPORT, 0.6f, 0.5f)
    sendMessage("§c"+message)
}

fun getTeamById(id: UUID): Team? {
    if (id in GameState.team1.members) return GameState.team1
    if (id in GameState.team2.members) return GameState.team2
    return null
}

inline fun forAllPlayers(func: (player: Player) -> Unit) {
    GameState.playerStatMap.keys.forEach { func(Bukkit.getPlayer(it) ?: return@forEach) }
}

inline fun ItemStack.editMeta(crossinline func: ItemMeta.() -> Unit): ItemStack {
    itemMeta = itemMeta?.apply(func)
    return this
}

@JvmName("editMetaM")
inline fun <reified M : ItemMeta> ItemStack.editMeta(crossinline func: M.() -> Unit): ItemStack {
    val first = itemMeta ?: return this
    val meta = first as? M ?: error("Specified ${M::class.simpleName}, found ${first::class.simpleName}")
    itemMeta = meta.apply(func)
    return this
}

operator fun Location.plus(other: Location) = Location(this.world, this.x + other.x, this.y + other.y, this.z + other.z)
operator fun Location.unaryPlus() = Location(this.world, +this.x, +this.y, +this.z)
operator fun Location.unaryMinus() = Location(this.world, -this.x, -this.y, -this.z)
operator fun Location.minus(other: Location) = this + -other

operator fun Vector.plus(other: Vector) = Vector(this.x + other.x, this.y + other.y, this.z + other.z)
operator fun Vector.unaryPlus() = Vector(+this.x, +this.y, +this.z)
operator fun Vector.unaryMinus() = Vector(-this.x, -this.y, -this.z)
operator fun Vector.minus(other: Vector) = this + -other

fun Player.playSound(sound: Sound, volume: Float, pitch: Float) = this.playSound(this.location, sound, volume, pitch)

fun Player.immobilise(duration: Int=100, magnitude: Int) {
    this.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, duration, magnitude))
    this.getAttribute(Attribute.JUMP_STRENGTH)!!.apply {
        removeModifier(nullAttribute)
        addModifier(nullAttribute)
    }
}

val Player.stats
    get() = GameState.playerStatMap[this.uniqueId]

object TermColour {
    val R = "\u001B[0m"
    val RED = "\u001B[31m"
    val GREEN = "\u001B[32m"
    val YELLOW = "\u001B[33m"
}

val Block.isFullCube: Boolean
    get() = collisionShape.boundingBoxes.singleOrNull() == fullCube