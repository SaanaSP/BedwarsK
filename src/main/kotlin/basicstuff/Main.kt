package basicstuff

import commands.Teams
import game.GamePhase
import game.GameState
import listeners.BlockBreakListener
import listeners.BlockPlaceListener
import listeners.ClickListener
import listeners.DamageListener
import listeners.EntityClickListener
import listeners.GrassListener
import listeners.HungerListener
import listeners.InventoryClickListener
import listeners.MoveListener
import listeners.PickupListener
import listeners.ShiftListener
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

var schedule = mutableMapOf<String, BukkitRunnable>()

lateinit var plugin: Main
    private set

class Main : JavaPlugin() {

    override fun onEnable() {
        plugin = this
        GameState.gamePhase = GamePhase.NONE
        //map = loadWorldIfExists("map")
        Bukkit.getPluginManager().registerEvents(GrassListener, this)
        Bukkit.getPluginManager().registerEvents(InventoryClickListener, this)
        Bukkit.getPluginManager().registerEvents(EntityClickListener, this)
        //Bukkit.getPluginManager().registerEvents(GoldBlockListener(), this)
        Bukkit.getPluginManager().registerEvents(BlockBreakListener, this)
        Bukkit.getPluginManager().registerEvents(BlockPlaceListener, this)
        Bukkit.getPluginManager().registerEvents(ClickListener, this)
        Bukkit.getPluginManager().registerEvents(DamageListener, this)
        Bukkit.getPluginManager().registerEvents(PickupListener, this)
        Bukkit.getPluginManager().registerEvents(HungerListener, this)
        Bukkit.getPluginManager().registerEvents(MoveListener, this)
        Bukkit.getPluginManager().registerEvents(ShiftListener, this)
        this.getCommand("start")!!.setExecutor(commands.CmdStart)
        this.getCommand("teams")!!.setExecutor(Teams())
        this.getCommand("cheats")!!.setExecutor(commands.Cheats)
        this.getCommand("item")!!.setExecutor(commands.Item)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
    }
}