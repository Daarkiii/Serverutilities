package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.PlaceHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.md_5.bungee.api.ProxyServer
import java.util.*

abstract class BungeeUser(
    override val id: Long,
    override val uuid: UUID,
    private val proxy: BungeeImpl,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long
) : User {

    override val name: String = this.proxiedPlayer.name

    abstract override val displayName: String

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
        proxy.adventure.player(this.proxiedPlayer).sendMessage(Message.Wrapper.wrap(msg))
    }

    /**
     * Sends the given message to the command sender
     * @param msg the message to send
     * @param placeHolder which should be replaced
     */
    override fun sendMessage(msg: String, vararg placeHolder: PlaceHolder) {
        proxy.adventure.player(this.proxiedPlayer).sendMessage(Message.Wrapper.wrap(msg, *placeHolder))
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
        proxy.adventure.player(this.proxiedPlayer).sendMessage(Message.Wrapper.wrap(miniMsg))
    }

    override val isOnline: Boolean
        get() =  true

    override fun addOnlineTime() {
        this.onlineTime = this.onlineTime + 1
    }

    abstract override fun connect(server: String)

    override fun kick(reason: String) {
        this.proxiedPlayer.disconnect(net.md_5.bungee.api.chat.TextComponent(reason))
    }

    override fun sendTabList(header: Component, footer: Component) {
        proxy.adventure.player(this.proxiedPlayer).sendPlayerListHeaderAndFooter(header, footer)
    }

    override val address: String
        get() {
            val player = this.proxiedPlayer ?: return ""

            return player.socketAddress.toString().split("/")[1].split(":")[0]
        }

    override val serverName: String
        get() {
            val player = this.proxiedPlayer ?: return ""

            return player.server.info.name
        }

    override val ping = this.proxiedPlayer.ping

    override fun executeCommand(cmd: String) {
        ProxyServer.getInstance().pluginManager.dispatchCommand(this.proxiedPlayer, cmd)
    }

    private val proxiedPlayer
        get() = ProxyServer.getInstance().getPlayer(uuid)

    override fun equals(other: Any?): Boolean {

        if(other == null)
            return false

        if(this === other)
            return true

        if(other !is User)
            return false

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}