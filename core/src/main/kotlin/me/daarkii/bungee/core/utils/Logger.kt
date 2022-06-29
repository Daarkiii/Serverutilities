package me.daarkii.bungee.core.utils

interface Logger {

    /**
     * Sends a message to the Console
     * @param msg the Message to send
     */
    fun sendConsoleMessage(msg: String)

    /**
     * Sends a message to every online player
     * @param msg the Message to send
     */
    fun sendMessage(msg: String)

    /**
     * Sends a message to every online player with the given permission
     * @param msg the Message to send
     * @param permission the permission which the player needs
     */
    fun sendMessage(msg: String, permission: String)

    /**
     * Sends a message to the Console if the Server is in the debug mode
     * @param msg the Message to send
     */
    fun debug(msg: String)

    /**
     * Sends a message to the Console when a small problem occurred
     * @param msg the Message to send
     */
    fun sendWarning(msg: String)

    /**
     * Sends a message to the console when the server has detected an error
     * @param msg the Message to send
     */
    fun sendError(msg: String)

}