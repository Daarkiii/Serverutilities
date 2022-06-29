package me.daarkii.bungee.core.`object`

import net.kyori.adventure.text.TextComponent

interface CommandSender {

    /**
     * Gets the name of the command sender
     * @return the name
     */
    fun getName() : String

    /**
     * Gets the colored name of the command sender
     * @return the colored name
     */
    fun getDisplayName() : String

    /**
     * Checks if the command sender has the given permission
     * @param permission the permission to check
     * @return true if the command sender has the permission
     */
    fun hasPermission(permission: String) : Boolean

    /**
     * Sends the given message to the command sender
     * @param msg the message to send
     */
    fun sendMessage(msg: String)

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    fun sendMessage(component: TextComponent)

    /**
     * Sends the given MiniMessage String  to the command sender
     * @param miniMsg the not deserialized MiniMessage String
     */
    fun sendMiniMessage(miniMsg: String)

}