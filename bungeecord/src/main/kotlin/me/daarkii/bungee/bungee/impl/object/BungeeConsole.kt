package me.daarkii.bungee.bungee.impl.`object`

import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.utils.PlaceHolder
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer

class BungeeConsole(
    private val adventure: BungeeAudiences,
    private val console: CommandSender
) : Console {

    override val name = "Console"

    override val displayName = "§c§l" + this.name

    override fun hasPermission(permission: String): Boolean {
        return this.console.hasPermission(permission)
    }

    override fun sendMessage(msg: String) {
        adventure.console().sendMessage(Message.Wrapper.wrap(msg))
    }

    /**
     * Sends the given message to the command sender
     * @param msg the message to send
     * @param placeHolder which should be replaced
     */
    override fun sendMessage(msg: String, vararg placeHolder: PlaceHolder) {
        adventure.console().sendMessage(Message.Wrapper.wrap(msg, *placeHolder))
    }

    override fun sendMessage(component: TextComponent) {
        adventure.console().sendMessage(component)
    }

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    override fun sendMessage(component: Component) {
        adventure.console().sendMessage(component)
    }

    override fun sendMiniMessage(miniMsg: String) {
        adventure.console().sendMessage(MiniMessage.miniMessage().deserialize(miniMsg))
    }

    /**
     * Executes the given command
     */
    override fun executeCommand(cmd: String) {
        ProxyServer.getInstance().pluginManager.dispatchCommand(this.console, cmd)
    }
}