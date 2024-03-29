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

import com.mongodb.client.model.Filters
import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.`object`.Group
import me.daarkii.bungee.core.storage.MongoDB
import org.bson.Document
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

class MongoGroupHandler(mongo: MongoDB) : GroupHandler {

    private val groupCache: MutableMap<String, Group> = ConcurrentHashMap()
    private val collection = mongo.getCollection("groups")

    /**
     * Creates a Group with the given parameters
     */
    override fun createGroup(name: String, potency: Int, permission: String, color: String): CompletableFuture<Group> {
        return this.getGroup(name).thenApply { group ->

            if(group != null)
                return@thenApply group

            val nextID = (collection.countDocuments() + 1).toInt()
            val created = Group(nextID, name, potency, permission, color, false)

            //add to the cache
            groupCache[name] = created

            //create in database
            this.safeGroup(created)

            created
        }
    }

    override val groups: Collection<Group> = groupCache.values

    /**
     * Gets a cached group object with the given name
     * @return the group object if the group is existing otherwise null
     */
    override fun getGroup(name: String): CompletableFuture<Group?> {
        return CompletableFuture.supplyAsync { groupCache[name.lowercase()] }
    }

    /**
     * Gets a cached group object with the given id
     * @return the group object if the group is existing otherwise null
     */
    override fun getGroup(id: Int): CompletableFuture<Group?> {
        return this.getName(id).thenApply {

            if(it == null)
                return@thenApply null

            this.getGroup(it).join()
        }
    }

    /**
     * Changes the name of the group in the cache
     */
    override fun changeGroupName(oldName: String, group: Group): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            groupCache.remove(oldName)
            groupCache[group.name] = group
        }
    }

    override val defaultGroup: Group
        get() = groups.stream().filter(Group::default).collect(Collectors.toList())[0]

    /**
     * Gets the name of the group with the id
     * @return the name of the group with the id or null
     */
    override fun getName(id: Int): CompletableFuture<String?> {
        return CompletableFuture.supplyAsync { collection.find(Filters.eq("id", id)).first()?.getString("name") }
    }

    /**
     * Safes a group in the database
     */
    override fun safeGroup(group: Group) {

        val document = Document()
            .append("id", group.id)
            .append("name", group.name)
            .append("potency", group.potency)
            .append("permission", group.permission)
            .append("color", group.color)
            .append("default", group.default)

        if(this.collection.find(Filters.eq("id", group.id)).first() != null)
            this.collection.replaceOne(Filters.eq("id", group.id), document)
        else
            this.collection.insertOne(document)
    }

    /**
     * Deletes a group out of the database && removes it from the cache
     */
    override fun deleteGroup(group: Group) {
        this.groupCache.remove(group.name)
        this.collection.deleteOne(Filters.eq("id", group.id))
    }

    /**
     * Loads every group on startup and safes them in the cache
     */
    override fun loadGroups() {

        //Check if the default group exist
        if(this.collection.find(Filters.eq("id", 1)).first() == null) {

            //Create default group it will be registered later
            val document = Document()
                .append("id", 1) //the id is everytime 1
                .append("name", "default")
                .append("potency", 0)
                .append("permission", "")
                .append("color", "&7")
                .append("default", true)

            this.collection.insertOne(document)
        }

        //Register every group which exists
        this.collection.find().iterator().forEach { document ->
            val group = Group(
                document.getInteger("id"),
                document.getString("name"),
                document.getInteger("potency"),
                document.getString("permission"),
                document.getString("color"),
                document.getBoolean("default"))

            this.groupCache[group.name] = group
        }
    }

    /**
     * Safes every group on server end
     */
    override fun safeGroups() {
        this.groupCache.values.forEach { group -> this.safeGroup(group) }
    }

}