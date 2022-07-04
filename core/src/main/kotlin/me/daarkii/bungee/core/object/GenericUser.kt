package me.daarkii.bungee.core.`object`

import net.kyori.adventure.text.Component
import java.util.UUID

interface GenericUser {

    /**
     * Gets the custom id of the user
     * @return the id
     */
    val id: Long

    /**
     * Gets the name of the offlineUser
     * @return the last known name of the User
     */
    val name: String

    /**
     * Gets the colored name of the offlineUser
     * @return the colored name or the normal name if the server is not using CloudNet or the player is online
     */
    val displayName: String

    /**
     * Gets the uuid of the user
     * @return the uuid
     */
    val uuid: UUID

    /**
     * Checks if the User is online
     * @return true if he is online
     */
    val isOnline: Boolean

    /**
     * Gets the timestamp where the user joined the first time
     */
    val firstJoin: Long

    /**
     * Gets the timestamp where the user joined the last time
     */
    val lastJoin: Long

    /**
     * Gets the online-time from the user in milliseconds
     */
    var onlineTime: Long
}