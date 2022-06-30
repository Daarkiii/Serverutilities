package me.daarkii.bungee.bungee.impl.`object`

import me.daarkii.bungee.core.`object`.Console
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.CommandSender

class BungeeConsole(
    private val adventure: BungeeAudiences,
    private val console: CommandSender
) : Console {

    override val name: String
        get() = "Console"

    override val displayName: String
        get() = "§c§l" + this.name

    override fun hasPermission(permission: String): Boolean {
        return this.console.hasPermission(permission)
    }

    override fun sendMessage(msg: String) {
        this.console.sendMessage(net.md_5.bungee.api.chat.TextComponent(msg))
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
}