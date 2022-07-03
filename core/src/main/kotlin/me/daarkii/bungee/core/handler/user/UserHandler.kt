package me.daarkii.bungee.core.handler.user

import me.daarkii.bungee.core.`object`.User
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface UserHandler {

    /**
     * 1 = id
     * 2 = uuid
     * 3 = name
     * 4 = first join
     * 5 = last join
     * 6 = online time (in minutes)
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
     * Gets the uuid from the given name
     *
     * @param name of the user
     */
    fun getUUID(name: String) : UUID

    /**
     * Gets the id from the given uuid
     *
     * @param uuid of the user
     */
    fun getID(uuid: UUID) : Long

    /**
     * Generates a new ID for the user object
     */
    fun generateID() : Long

    /**
     * Check if the given name is multiple times in the database,
     * so we need to fetch the name with the uuid
     *
     * @param name of the user
     */
    fun checkExistingNames(name: String)

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
     * @param id the id of the user
     */
    fun loadData(id: Long) : CompletableFuture<Array<out Any>>

    /**
     * Creates the User in the database
     *
     * @param uuid of the user
     * @param name of the user
     */
    fun createUser(uuid: UUID, name: String) : CompletableFuture<Void>

}