package me.daarkii.bungee.bungee.impl

import me.daarkii.bungee.bungee.BungeeHook
import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.utils.Logger
import net.md_5.bungee.api.chat.TextComponent

class BungeeLogger(private val proxy: BungeeHook) : Logger {

    /**
     * Sends a message to the Console
     * @param msg the Message to send
     */
    override fun sendConsoleMessage(msg: String) {
        proxy.proxy.console.sendMessage(TextComponent(msg))
    }

    /**
     * Sends a message to every online player
     * @param msg the Message to send
     */
    override fun sendMessage(msg: String) {
        proxy.proxy.players.forEach { player ->
            player.sendMessage(TextComponent(msg))
        }
    }

    /**
     * Sends a message to every online player with the given permission
     * @param msg the Message to send
     * @param permission the permission which the player needs
     */
    override fun sendMessage(msg: String, permission: String) {
        proxy.proxy.players.forEach { player ->
            if(player.hasPermission(permission))
                player.sendMessage(TextComponent(msg))
        }
    }

    /**
     * Sends a message to the Console if the Server is in the debug mode
     * @param msg the Message to send
     */
    override fun debug(msg: String) {
        if(!BungeeSystem.getInstance().isInDebugMode())
            return

        proxy.proxy.console.sendMessage(TextComponent("§a§lBungee-DEBUG §8» §6$msg"))
    }

    /**
     * Sends a message to the Console when a small problem occurred
     * @param msg the Message to send
     */
    override fun sendWarning(msg: String) {
        proxy.proxy.console.sendMessage(TextComponent("§6§lBungee-WARNING §8» §6$msg"))
    }

    /**
     * Sends a message to the console when the server has detected an error
     * @param msg the Message to send
     */
    override fun sendError(msg: String) {
        proxy.proxy.console.sendMessage(TextComponent("§c§lBungee-ERROR §8» §c$msg"))
    }
}