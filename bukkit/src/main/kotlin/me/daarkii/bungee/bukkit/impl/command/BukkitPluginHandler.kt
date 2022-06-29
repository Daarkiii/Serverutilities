package me.daarkii.bungee.bukkit.impl.command

import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.command.PluginHandler
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin

class BukkitPluginHandler(private val bukkit: JavaPlugin) : PluginHandler {

    override fun registerCommand(command: Command) {

        val field = bukkit.server.javaClass.getDeclaredField("commandMap")

        field.isAccessible = true
        val map = field.get(bukkit.server) as CommandMap

        map.register(command.name, BukkitCommand(command))
    }

}