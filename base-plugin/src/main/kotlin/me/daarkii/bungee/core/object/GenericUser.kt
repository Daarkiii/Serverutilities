package me.daarkii.bungee.core.`object`

import java.util.UUID

interface GenericUser {

    /**
     * Gets the uuid of the user
     * @return the uuid
     */
    fun getUUID() : UUID

    /**
     * Checks if the User is online
     * @return true if he is online
     */
    fun isOnline() : Boolean

}