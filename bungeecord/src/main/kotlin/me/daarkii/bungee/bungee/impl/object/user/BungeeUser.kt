package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.core.`object`.User
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ProxyServer
import java.util.*

class BungeeUser(private val uuidImpl: UUID, private val proxy: BungeeImpl) : User {

    override val name: String
        get() =  this.proxiedPlayer.name

    override val displayName: String
        get() =  this.proxiedPlayer.displayName

    /**
     * Checks if the command sender has the given permission
     * @param permission the permission to check
     * @return true if the command sender has the permission
     */
    override fun hasPermission(permission: String): Boolean {
        return this.proxiedPlayer.hasPermission(permission)
    }

    /**
     * Sends the given message to the command sender
     * @param msg the message to send
     */
    override fun sendMessage(msg: String) {
        this.proxiedPlayer.sendMessage(net.md_5.bungee.api.chat.TextComponent(msg))
    }

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    override fun sendMessage(component: TextComponent) {
        proxy.getAdventure().player(this.proxiedPlayer).sendMessage(component)
    }

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    override fun sendMessage(component: Component) {
        proxy.getAdventure().player(this.proxiedPlayer).sendMessage(component)
    }

    /**
     * Sends the given MiniMessage String  to the command sender
     * @param miniMsg the not deserialized MiniMessage String
     */
    override fun sendMiniMessage(miniMsg: String) {
        proxy.getAdventure().player(this.proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(miniMsg))
    }

    override val uuid: UUID
        get() =  uuidImpl

    override val isOnline: Boolean
        get() =  true

    override fun connect(server: String) {
    }

    override fun kick(reason: String) {
        this.proxiedPlayer.disconnect(net.md_5.bungee.api.chat.TextComponent(reason))
    }

    override fun sendTabList(header: Component, footer: Component) {
        proxy.getAdventure().player(this.proxiedPlayer).sendPlayerListHeaderAndFooter(header, footer)
    }

    override val address: String
        get() =  ""

    override val serverName: String
        get() =  this.proxiedPlayer.server.info.name

    override val ping: Int
        get() = this.proxiedPlayer.ping

    private val proxiedPlayer
        get() = ProxyServer.getInstance().getPlayer(uuid)
}