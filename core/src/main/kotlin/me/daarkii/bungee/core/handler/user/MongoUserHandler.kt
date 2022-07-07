package me.daarkii.bungee.core.handler.user

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.storage.MongoDB
import me.daarkii.bungee.core.utils.fetcher.UUIDFetcher
import org.bson.Document
import java.util.*
import java.util.concurrent.CompletableFuture

class MongoUserHandler(mongo: MongoDB) : UserHandler {

    private val collection: MongoCollection<Document> = mongo.getCollection("user")

    /**
     * Check if an uuid is registered in the database
     *
     * @param uuid of the user
     */
    override fun isExist(uuid: UUID): Boolean {
        return collection.find(Filters.eq("uuid", uuid.toString())).first() != null
    }

    /**
     * Check if an id is registered in the database
     *
     * @param id of the user
     */
    override fun isExist(id: Long): Boolean {
        return collection.find(Filters.eq("id", id)).first() != null
    }

    /**
     * Gets the uuid from the given name
     *
     * @param name of the user
     */
    override fun getUUID(name: String): UUID {

        val document: Document? = collection.find(Filters.eq("name", name)).first()

        if(!document.isNullOrEmpty()) {

            this.checkExistingNames(name)

            return UUID.fromString(document.getString("uuid"))
        }

        return UUIDFetcher.getUUID(name)
    }

    /**
     * Gets the id from the given uuid
     *
     * @param uuid of the user
     */
    override fun getID(uuid: UUID): Long {
        return collection.find(Filters.eq("uuid", uuid.toString())).first()?.getLong("id") ?: 0
    }

    /**
     * Generates a new ID for the user object
     */
    override fun generateID(): Long {

        var id = Random().nextLong()

        while (this.isExist(id)) {
            id = Random().nextLong()
        }

        return id
    }

    /**
     * Check if the given name is multiple times in the database,
     * so we need to fetch the name with the uuid
     *
     * @param name of the user
     */
    override fun checkExistingNames(name: String) {

        if(collection.countDocuments(Filters.eq("name", name)) <= 1)
            return

        collection.find(Filters.eq("name", name)).iterator().forEach { document ->

            val uuid = UUID.fromString(document.getString("uuid"))
            val documentName = document.getString("name")
            val newName = UUIDFetcher.getName(uuid)

            if(documentName.equals(newName, ignoreCase = true))
                return

            //reset the other name
            val filter = Filters.eq("uuid", uuid)
            val update = Updates.combine(Updates.set("name", newName))
            val options = UpdateOptions().upsert(true)

            //Update in database
            collection.updateOne(filter, update, options)
        }
    }

    /**
     * Safes all stored data from the cache in the database
     * Mostly used when the user disconnects
     *
     * @param user the user to safe
     */
    override fun saveData(user: User) {

        val document = Document()
            .append("id", user.id)
            .append("uuid", user.uuid.toString())
            .append("name", user.name)
            .append("firstJoin", user.firstJoin)
            .append("lastJoin", user.lastJoin)
            .append("onlineTime", user.onlineTime)
            .append("lastGroup", user.highestGroup.id)

        this.collection.updateOne(Filters.eq("id", user.id), document)
    }

    /**
     * Loads all data from the database and insert it in the cache
     *
     * @param id the user to safe
     */
    override fun loadData(id: Long): CompletableFuture<Array<out Any>> {
        return CompletableFuture.supplyAsync {

            val document = collection.find(Filters.eq("id", id)).first()!!

            arrayOf(
                id,
                UUID.fromString(document.getString("uuid")),
                document.getString("name"),
                document.getLong("firstJoin"),
                document.getLong("lastJoin"),
                document.getLong("onlineTime"),
                document.getInteger("lastGroup")
            )

        }
    }

    /**
     * Creates the User in the database
     *
     * @param uuid of the user
     * @param name of the user
     */
    override fun createUser(uuid: UUID, name: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {

            if (!this.isExist(uuid)) {

                val document = Document()
                    .append("id", this.generateID())
                    .append("uuid", uuid.toString())
                    .append("name", name)
                    .append("firstJoin", System.currentTimeMillis())
                    .append("lastJoin", System.currentTimeMillis())
                    .append("onlineTime", (0).toLong())
                    .append("lastGroup", 1)

                collection.insertOne(document)
            }

        }
    }
}