package me.daarkii.bungee.bungee

import me.daarkii.bungee.bungee.impl.BungeeImpl
import net.md_5.bungee.api.plugin.Plugin

/**
 * Startup Class for the BungeeCord Platform
 */
class BungeeHook : Plugin() {

    override fun onEnable() {
        BungeeImpl(this)
    }

    override fun onDisable() {
    }

}