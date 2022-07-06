package me.daarkii.bungee.bungee

import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.bungee.listener.PlayerListener
import net.md_5.bungee.api.plugin.Plugin

/**
 * Startup Class for the BungeeCord Platform
 */
class BungeeHook : Plugin() {

    private lateinit var impl: BungeeImpl

    override fun onEnable() {
        impl = BungeeImpl(this)
    }

    override fun onDisable() {
        impl.stop()
    }

}