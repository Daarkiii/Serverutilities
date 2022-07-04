package me.daarkii.bungee.core.handler.group

import com.mongodb.client.model.Filters
import me.daarkii.bungee.core.`object`.Group
import me.daarkii.bungee.core.storage.MongoDB
import me.daarkii.bungee.core.utils.TripleMap
import org.bson.Document
import java.util.concurrent.CompletableFuture

class MongoGroupHandler(mongo: MongoDB) : GroupHandler {

    private val groupCache: TripleMap<Long, String, Group> = TripleMap()
    private val collection = mongo.getCollection("groups")

    /**
     * Creates a Group with the given parameters
     */
    override fun createGroup(name: String, potency: Int, permission: String, color: String): CompletableFuture<Group> {
        return this.getGroup(name).thenApply { group ->

            if(group != null)
                return@thenApply group

            val nextID = (groupCache.size + 1).toLong()
            val created = Group(nextID, name, potency, permission, color)

            //add to the cache
            groupCache.insert(nextID, name, created)

            //create in database
            this.safeGroup(created)

            created
        }
    }

    /**
     * Gets a cached group object with the given name
     * @return the group object if the group is existing otherwise null
     */
    override fun getGroup(name: String): CompletableFuture<Group?> {
        return CompletableFuture.supplyAsync {
            groupCache.get(name)
        }
    }

    /**
     * Gets a cached group object with the given id
     * @return the group object if the group is existing otherwise null
     */
    override fun getGroup(id: Long): CompletableFuture<Group?> {
        return CompletableFuture.supplyAsync {
            groupCache.get(id)
        }
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

        if(this.collection.find(Filters.eq("id", group.id)).first() != null)
            this.collection.replaceOne(Filters.eq("id", group.id), document)
        else
            this.collection.insertOne(document)
    }

    /**
     * Loads every group on startup and safes them in the cache
     */
    override fun loadGroups() {
        this.collection.find().iterator().forEach { document ->
            val group = Group(
                document.getLong("id"),
                document.getString("name"),
                document.getInteger("potency"),
                document.getString("permission"),
                document.getString("color"))

            this.groupCache.insert(group.id, group.name, group)
        }
    }

    /**
     * Safes every group on server end
     */
    override fun safeGroups() {
        this.groupCache.values.forEach { group -> this.safeGroup(group) }
    }

}