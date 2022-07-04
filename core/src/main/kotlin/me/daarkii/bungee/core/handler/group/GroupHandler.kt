package me.daarkii.bungee.core.handler.group

import me.daarkii.bungee.core.`object`.Group
import java.util.concurrent.CompletableFuture

interface GroupHandler {

    /**
     * Creates a Group with the given parameters
     */
    fun createGroup(name: String, potency: Int, permission: String, color: String) : CompletableFuture<Group>

    /**
     * Gets a cached group object with the given name
     * @return the group object if the group is existing otherwise null
     */
    fun getGroup(name: String) : CompletableFuture<Group?>

    /**
     * Gets a cached group object with the given id
     * @return the group object if the group is existing otherwise null
     */
    fun getGroup(id: Long) : CompletableFuture<Group?>

    /**
     * Safes a group in the database
     */
    fun safeGroup(group: Group)

    /**
     * Loads every group on startup and safes them in the cache
     */
    fun loadGroups()

    /**
     * Safes every group on server end
     */
    fun safeGroups()

}