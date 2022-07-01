package me.daarkii.bungee.bungee.listener

import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.utils.PlaceHolder
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.event.ServerSwitchEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler

class PlayerListener(private val plugin: Plugin): Listener {

    @EventHandler
    fun handleLogin(event: ServerSwitchEvent) {

        val player = event.player
        val audience = BungeeImpl.instance.adventure.player(player)

        val header = Message.Wrapper.wrap(Message.instance.config.getString("messages.tablist.header"),
            PlaceHolder("players", Component.text(player.server.info.players.size)),
            PlaceHolder("maxplayers", Component.text(plugin.proxy.onlineCount)),
            PlaceHolder("server", Component.text(player.server.info.name)))

        val footer = Message.Wrapper.wrap(Message.instance.config.getString("messages.tablist.footer"),
            PlaceHolder("players", Component.text(player.server.info.players.size)),
            PlaceHolder("maxplayers", Component.text(plugin.proxy.onlineCount)),
            PlaceHolder("server", Component.text(player.server.info.name)))

        audience.sendPlayerListHeaderAndFooter(header, footer)
    }

}