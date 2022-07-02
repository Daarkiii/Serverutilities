package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.core.`object`.User
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ProxyServer
import java.util.*

class BungeeUser(
    override val id: Long,
    override val uuid: UUID,
    private val proxy: BungeeImpl,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long
) : User {

    override val name: String = this.proxiedPlayer.name

    override val displayName: String = this.proxiedPlayer.displayName

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
        proxy.adventure.player(this.proxiedPlayer).sendMessage(component)
    }

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    override fun sendMessage(component: Component) {
        proxy.adventure.player(this.proxiedPlayer).sendMessage(component)
    }

    /**
     * Sends the given MiniMessage String  to the command sender
     * @param miniMsg the not deserialized MiniMessage String
     */
    override fun sendMiniMessage(miniMsg: String) {
        proxy.adventure.player(this.proxiedPlayer).sendMessage(MiniMessage.miniMessage().deserialize(miniMsg))
    }

    override val isOnline: Boolean
        get() =  true

    override fun addOnlineTime() {
        this.onlineTime = this.onlineTime + 1
    }

    override fun connect(server: String) {
    }

    override fun kick(reason: String) {
        this.proxiedPlayer.disconnect(net.md_5.bungee.api.chat.TextComponent(reason))
    }

    override fun sendTabList(header: Component, footer: Component) {
        proxy.adventure.player(this.proxiedPlayer).sendPlayerListHeaderAndFooter(header, footer)
    }

    override val address = ""

    override val serverName: String = this.proxiedPlayer.server.info.name

    override val ping = this.proxiedPlayer.ping

    private val proxiedPlayer
        get() = ProxyServer.getInstance().getPlayer(uuid)
}