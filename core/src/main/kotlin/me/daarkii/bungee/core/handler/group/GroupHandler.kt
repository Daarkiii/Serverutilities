/*
 * Copyright 2022 original authors & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
     * Changes the name of the group in the cache
     */
    fun changeGroupName(oldName: String, group: Group) : CompletableFuture<Void>

    /**
     * Gets a cached group object with the given id
     * @return the group object if the group is existing otherwise null
     */
    fun getGroup(id: Int) : CompletableFuture<Group?>

    /**
     * Gets the cached default group
     * @return the group object
     */
    val defaultGroup: Group

    /**
     * Gets the name of the group with the id
     * @return the name of the group with the id or null
     */
    fun getName(id: Int) : CompletableFuture<String?>

    /**
     * Safes a group in the database
     */
    fun safeGroup(group: Group)

    /**
     * Deletes a group out of the database && removes it from the cache
     */
    fun deleteGroup(group: Group)

    /**
     * Gets all groups from the cache
     */
    val groups: Collection<Group>

    /**
     * Loads every group on startup and safes them in the cache
     */
    fun loadGroups()

    /**
     * Safes every group on server end
     */
    fun safeGroups()

}