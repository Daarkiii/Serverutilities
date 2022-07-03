package me.daarkii.bungee.core.handler.user

import me.daarkii.bungee.core.`object`.User
import java.util.*
import java.util.concurrent.CompletableFuture

class SQLUserHandler : UserHandler {
    /**
     * Check if an uuid is registered in the database
     *
     * @param uuid of the user
     */
    override fun isExist(uuid: UUID): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Check if an id is registered in the database
     *
     * @param id of the user
     */
    override fun isExist(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Gets the uuid from the given name
     *
     * @param name of the user
     */
    override fun getUUID(name: String): UUID {
        TODO("Not yet implemented")
    }

    /**
     * Gets the id from the given uuid
     *
     * @param uuid of the user
     */
    override fun getID(uuid: UUID): Long {
        TODO("Not yet implemented")
    }

    /**
     * Generates a new ID for the user object
     */
    override fun generateID(): Long {
        TODO("Not yet implemented")
    }

    /**
     * Check if the given name is multiple times in the database,
     * so we need to fetch the name with the uuid
     *
     * @param name of the user
     */
    override fun checkExistingNames(name: String) {
        TODO("Not yet implemented")
    }

    /**
     * Safes all stored data from the cache in the database
     * Mostly used when the user disconnects
     *
     * @param user the user to safe
     */
    override fun saveData(user: User) {
        TODO("Not yet implemented")
    }

    /**
     * Loads all data from the database and insert it in the cache
     *
     * @param id the id of the user
     */
    override fun loadData(id: Long): CompletableFuture<Array<out Any>> {
        TODO("Not yet implemented")
    }

    /**
     * Creates the User in the database
     *
     * @param uuid of the user
     * @param name of the user
     */
    override fun createUser(uuid: UUID, name: String): CompletableFuture<Void> {
        TODO("Not yet implemented")
    }
}