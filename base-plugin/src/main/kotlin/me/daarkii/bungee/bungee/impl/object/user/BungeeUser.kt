package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.bungee.BungeeHook
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.Settings
import net.kyori.adventure.text.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

class BungeeUser(
    private val player: ProxiedPlayer,
    private val proxy: BungeeHook
) : User {

    override fun connect(server: String) {

        if(Settings.instance.isCloudNetActive()) {


        }

    }

    override fun kick(reason: String) {
        TODO("Not yet implemented")
    }

    override fun getAddress(): String {
        TODO("Not yet implemented")
    }

    override fun getServerName(): String {
        TODO("Not yet implemented")
    }

    override fun getPing(): Int {
        TODO("Not yet implemented")
    }

    /**
     * Gets the name of the command sender
     * @return the name
     */
    override fun getName(): String {
        TODO("Not yet implemented")
    }

    /**
     * Gets the colored name of the command sender
     * @return the colored name
     */
    override fun getDisplayName(): String {
        TODO("Not yet implemented")
    }

    /**
     * Checks if the command sender has the given permission
     * @param permission the permission to check
     * @return true if the command sender has the permission
     */
    override fun hasPermission(permission: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Sends the given message to the command sender
     * @param msg the message to send
     */
    override fun sendMessage(msg: String) {
        TODO("Not yet implemented")
    }

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    override fun sendMessage(component: TextComponent) {
        TODO("Not yet implemented")
    }

    /**
     * Sends the given MiniMessage String  to the command sender
     * @param miniMsg the not deserialized MiniMessage String
     */
    override fun sendMiniMessage(miniMsg: String) {
        TODO("Not yet implemented")
    }

    /**
     * Gets the uuid of the user
     * @return the uuid
     */
    override fun getUUID(): UUID {
        TODO("Not yet implemented")
    }

    /**
     * Checks if the User is online
     * @return true if he is online
     */
    override fun isOnline(): Boolean {
        TODO("Not yet implemented")
    }
}