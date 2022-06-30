package me.daarkii.bungee.bungee.impl.command

import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.command.PluginHandler
import net.md_5.bungee.api.plugin.Plugin

class BungeePluginHandler(private val proxy: Plugin) : PluginHandler {

    override fun registerCommand(command: Command) {
        proxy.proxy.pluginManager.registerCommand(proxy, BungeeCommand(command))
    }

}