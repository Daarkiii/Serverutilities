package me.daarkii.bungee.bukkit

import me.daarkii.bungee.bukkit.impl.BukkitImpl
import org.bukkit.plugin.java.JavaPlugin

/**
 * Startup Class for the Bukkit Platform
 */
class BukkitHook  : JavaPlugin() {

    override fun onEnable() {
        BukkitImpl(this)
    }

    override fun onDisable() {
    }

}