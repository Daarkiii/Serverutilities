package me.daarkii.bungee.bungee.impl.`object`

import me.daarkii.bungee.core.`object`.Console
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.CommandSender

class BungeeConsole(
    private val adventure: BungeeAudiences,
    private val console: CommandSender
) : Console {

    override fun getName(): String {
        return "Console"
    }

    override fun getDisplayName(): String {
        return "§c§l" + this.getName()
    }

    override fun hasPermission(permission: String): Boolean {
        return this.console.hasPermission(permission)
    }

    override fun sendMessage(msg: String) {
        this.console.sendMessage(net.md_5.bungee.api.chat.TextComponent(msg))
    }

    override fun sendMessage(component: TextComponent) {
        adventure.console().sendMessage(component)
    }

    override fun sendMiniMessage(miniMsg: String) {
        adventure.console().sendMessage(MiniMessage.miniMessage().deserialize(miniMsg))
    }
}