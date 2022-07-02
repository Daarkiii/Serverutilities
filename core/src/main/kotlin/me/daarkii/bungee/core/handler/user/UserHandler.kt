package me.daarkii.bungee.core.handler.user

import me.daarkii.bungee.core.`object`.User
import java.util.UUID

interface UserHandler {

    /**
     * 1 = id
     * 2 = uuid
     * 3 = name
     * 4 = first join
     * 5 = last join
     * 6 = online time (in seconds)
     */

    /**
     * Check if an uuid is registered in the database
     *
     * @param uuid of the user
     */
    fun isExist(uuid: UUID) : Boolean

    /**
     * Check if an id is registered in the database
     *
     * @param id of the user
     */
    fun isExist(id: Long) : Boolean

    /**
     * Safes all stored data from the cache in the database
     * Mostly used when the user disconnects
     *
     * @param user the user to safe
     */
    fun saveData(user: User)

    /**
     * Loads all data from the database and insert it in the cache
     *
     * @param user the user to safe
     */
    fun loadData(user: User)

}