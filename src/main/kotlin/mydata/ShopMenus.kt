package mydata

import basicstuff.Heads
import basicstuff.Keys
import game.GameState
import game.ShopHeader
import game.ShopItem
import game.buildShopMenu
import game.grantByTeam
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType
import utils.arbItem
import utils.editMeta
import utils.makeHead
import utils.team

val shopHeaders = listOf(
    ShopHeader(
        "shop_header_blocks",
        arbItem(Material.BRICKS, 1, "§bBlocks"),
        ShopPage.BLOCKS
    ),
    ShopHeader(
        "shop_header_weapons",
        arbItem(Material.WOODEN_SWORD, 1, "§cWeapons"),
        ShopPage.WEAPONS
    ),
    ShopHeader(
        "shop_header_tools",
        arbItem(Material.IRON_PICKAXE, 1, "§eTools"),
        ShopPage.TOOLS
    ),
    ShopHeader(
        "shop_header_armour",
        arbItem(Material.CHAINMAIL_CHESTPLATE, 1, "§3Armour"),
        ShopPage.ARMOUR
    ),
    ShopHeader(
        "shop_header_utility",
        arbItem(Material.ENDER_PEARL, 1, "§aUtility"),
        ShopPage.UTILITY
    ),
    ShopHeader(
        "shop_header_upgrades",
        arbItem(Material.NETHER_STAR, 1, "§dUpgrades"),
        ShopPage.ARMOUR
    )
).associateBy { it.name }



val shopItems = listOf(

    ShopItem(
        "shop_wool",
        arbItem(Material.WHITE_WOOL, 16, "§rWool"),
        ShopPage.BLOCKS,
        10,
        Currency.GOLD,
        true
    ) { player ->
        grantByTeam(
            player,
            ItemStack(Material.LIGHT_BLUE_WOOL, 16),
            ItemStack(Material.PINK_WOOL, 16)
        )
    },
    ShopItem(
        "shop_wood",
        arbItem(Material.PALE_OAK_PLANKS, 8, "§rWood"),
        ShopPage.BLOCKS,
        100,
        Currency.GOLD,
        true
    ) { player ->
        grantByTeam(
            player,
            ItemStack(Material.WARPED_PLANKS, 8),
            ItemStack(Material.CRIMSON_PLANKS, 8)
        )
    },
    ShopItem(
        "shop_stone",
        arbItem(Material.COBBLESTONE, 4, "§rStone"),
        ShopPage.BLOCKS,
        1250,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.COBBLESTONE, 4))
    },
    ShopItem(
        "shop_end_stone",
        arbItem(Material.END_STONE, 2, "§rEnd Stone"),
        ShopPage.BLOCKS,
        2,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.END_STONE, 2))
    },
    ShopItem(
        "shop_obsidian",
        arbItem(Material.OBSIDIAN, 1, "§rObsidian"),
        ShopPage.BLOCKS,
        8,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.OBSIDIAN, 1))
    },

    //weapons
    ShopItem(
        "shop_wood_sword",
        arbItem(Material.WOODEN_SWORD, 1, "§rWood Sword"),
        ShopPage.WEAPONS,
        50,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.WOODEN_SWORD, 1).editMeta {
            isUnbreakable = true
        })
    },
    ShopItem(
        "shop_stone_sword",
        arbItem(Material.STONE_SWORD, 1, "§rStone Sword"),
        ShopPage.WEAPONS,
        450,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.STONE_SWORD, 1).editMeta {
            isUnbreakable = true
        })
    },
    ShopItem(
        "shop_iron_sword",
        arbItem(Material.IRON_SWORD, 1, "§rIron Sword"),
        ShopPage.WEAPONS,
        2,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.IRON_SWORD, 1).editMeta {
            isUnbreakable = true
        })
    },
    ShopItem(
        "shop_diamond_sword",
        arbItem(Material.DIAMOND_SWORD, 1, "§rDiamond Sword"),
        ShopPage.WEAPONS,
        8,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.DIAMOND_SWORD, 1).editMeta {
            isUnbreakable = true
        })
    },
    ShopItem(
        "shop_bow",
        arbItem(Material.BOW, 1, "§rBow"),
        ShopPage.WEAPONS,
        2,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.BOW, 1).editMeta<Damageable> {
            damage = 359
        }
        )
    },
    ShopItem(
        "shop_arrows",
        arbItem(Material.ARROW, 2, "§rArrows"),
        ShopPage.WEAPONS,
        250,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.ARROW, 2))
    },
    ShopItem(
        "shop_punch_fist",
        arbItem(
            makeHead(Heads.punching_gloves),
            "§rPunching Gloves",
            listOf("§r§8Hitting someone twice", "§r§8knocks them back")
        ),
        ShopPage.WEAPONS,
        500,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(makeHead(Heads.punching_gloves).editMeta {
            setDisplayName("§rPunching Gloves")
            persistentDataContainer.set(Keys.itemsKey, PersistentDataType.STRING, "punch_fist")
        })
    },

    //tools
    ShopItem(
        "shop_shears",
        arbItem(Material.SHEARS, 1, "§rShears"),
        ShopPage.TOOLS,
        450,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.SHEARS, 1).editMeta {
            isUnbreakable = true
        })
    },
    ShopItem(
        "shop_wood_pick",
        arbItem(Material.WOODEN_PICKAXE, 1, "§rWooden Pickaxe", listOf("§r§8Can break up to 10", "§r§8blocks")),
        ShopPage.TOOLS,
        1000,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.WOODEN_PICKAXE, 1).apply {
            itemMeta = (itemMeta!! as Damageable).apply {
                damage = 49
            }
        })
    },
    ShopItem(
        "shop_diamond_pick",
        arbItem(Material.DIAMOND_PICKAXE, 1, "§rDiamond Pickaxe", listOf("§r§8Can break up to 2", "§r§8blocks")),
        ShopPage.TOOLS,
        2,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.DIAMOND_PICKAXE, 1).editMeta<Damageable> {
            damage = 1559
        })
    },

    //utility
    ShopItem(
        "shop_platform",
        arbItem(Material.SNOW, 1, "§rPlatform", listOf("§r§8Catches you when", "§r§8you fall")),
        ShopPage.UTILITY,
        50,
        Currency.GOLD,
        true
    )
    { player -> player.inventory.addItem(Items.platform.item) },

    ShopItem(
        "shop_boost",
        arbItem(Material.FIREWORK_ROCKET, 1, "§rRocket Boost"),
        ShopPage.UTILITY,
        800,
        Currency.GOLD,
        true
    )
    { player -> player.inventory.addItem(Items.boost.item) },

    ShopItem(
        "shop_heal_apple",
        arbItem(Material.GOLDEN_APPLE, 1, "§rGolden Apple", listOf("§r§8Heals you and", "§r§8nearby teammates")),
        ShopPage.UTILITY,
        1,
        Currency.STARS,
        true
    )
    { player -> player.inventory.addItem(ItemStack(Material.GOLDEN_APPLE, 1)) },

    ShopItem(
        "shop_tnt",
        arbItem(
            Material.TNT,
            1,
            "§rTNT",
            listOf("§r§8Punch when placed to prime", "§r§8Punch when primed to shoot", "§r§8Shift-punch to pick up")
        ),
        ShopPage.UTILITY,
        2000,
        Currency.GOLD,
        true
    )
    { player -> player.inventory.addItem(ItemStack(Material.TNT, 1)) },

    ShopItem(
        "shop_pearl",
        arbItem(Material.ENDER_PEARL, 1, "§rEnder Pearl"),
        ShopPage.UTILITY,
        4,
        Currency.STARS,
        true
    )
    { player -> player.inventory.addItem(ItemStack(Material.ENDER_PEARL, 1)) },

    ShopItem(
        "shop_invis_cloak",
        arbItem(
            Material.CHAINMAIL_CHESTPLATE,
            1,
            "§r§fInvisibility Cloak",
            listOf("§r§8Gives you invisibility", "§r§8for 20 seconds")
        ).editMeta {
            setEnchantmentGlintOverride(true)
        },
        ShopPage.UTILITY,
        4,
        Currency.STARS,
        true
    )
    { player -> player.inventory.addItem(Items.invis_cloak.item) },

    ShopItem(
        "shop_traps",
        arbItem(Material.IRON_TRAPDOOR, 1, "§rTraps", listOf("§r§8Click to view more")),
        ShopPage.UTILITY,
        0,
        Currency.FREE,
        true
    ) { player ->
        player.openInventory(trapsMenu)
    },
    ShopItem(
        "shop_strike",
        arbItem(Material.GUARDIAN_SPAWN_EGG, 1, "§rRemote Strike", listOf("§r§8Lets you select a", "§r§8location for an airstrike")),
        ShopPage.UTILITY,
        10,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.GUARDIAN_SPAWN_EGG, 1).editMeta {
            setDisplayName("§rRemote Strike")
            persistentDataContainer.set(Keys.itemsKey, PersistentDataType.STRING, "strike")
        })
    },

    //traps
    ShopItem(
        "trap_slowness",
        arbItem(Material.SUSPICIOUS_SAND, 1, "§rWeak Trap", listOf("§r§8Creates a trap that", "§r§8slows temporarily")),
        ShopPage.TRAPS,
        200,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.SUSPICIOUS_SAND, 1).editMeta {
            setDisplayName("§rWeak Trap")
            persistentDataContainer.set(Keys.itemsKey, PersistentDataType.STRING, "trap_slowness")
            setMaxStackSize(1)
        })
    },
    ShopItem(
        "trap_launch",
        arbItem(Material.PISTON, 1, "§rLaunch Trap", listOf("§r§8Creates a trap that", "§r§8launches a player back")),
        ShopPage.TRAPS,
        750,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.PISTON, 1).editMeta {
            setDisplayName("§rLaunch Trap")
            persistentDataContainer.set(Keys.itemsKey, PersistentDataType.STRING, "trap_launch")
            setMaxStackSize(1)
        })
    },
    ShopItem(
        "trap_snare",
        arbItem(Material.TARGET, 1, "§rSnare Trap", listOf("§r§8Creates a trap that", "§r§8holds a player in place")),
        ShopPage.TRAPS,
        1000,
        Currency.GOLD,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.TARGET, 1).editMeta {
            setDisplayName("§rSnare Trap")
            persistentDataContainer.set(Keys.itemsKey, PersistentDataType.STRING, "trap_snare")
            setMaxStackSize(1)
        })
    },
    ShopItem(
        "trap_reveal",
        arbItem(
            Material.SHROOMLIGHT,
            1,
            "§rGlow Trap",
            listOf("§r§8Creates a trap that", "§r§8slows and reveals", "§r§8a player")
        ),
        ShopPage.TRAPS,
        2,
        Currency.STARS,
        true
    ) { player ->
        player.inventory.addItem(ItemStack(Material.SHROOMLIGHT, 1).editMeta {
            setDisplayName("§rGlow Trap")
            persistentDataContainer.set(Keys.itemsKey, PersistentDataType.STRING, "trap_reveal")
            setMaxStackSize(1)
        })
    },
    ShopItem(
        "trap_clear",
        arbItem(
            Material.BARRIER,
            1,
            "§rClear Traps",
            listOf("§r§8Clears all your", "§r§8team's traps")
        ),
        ShopPage.TRAPS,
        0,
        Currency.FREE,
        true
    ) { player ->
        GameState.traps.values.filter {it.team == player.team}.forEach {it.disarm()}
        player.team!!.trapCount = 0
    },
).associateBy{it.name}




val shopMenus = ShopPage.entries.associateWith(::buildShopMenu)

val trapsMenu = buildShopMenu(ShopPage.TRAPS)