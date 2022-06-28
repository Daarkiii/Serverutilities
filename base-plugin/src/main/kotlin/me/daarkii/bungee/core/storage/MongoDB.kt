package me.daarkii.bungee.core.storage

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

class MongoDB(private val url: String) {

    private var mongoClient: MongoClient? = null
    private var mongoDatabase: MongoDatabase? = null

    fun connect(database: String) {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(url)
        }
        mongoDatabase = database.let { mongoClient?.getDatabase(it) }
    }

    fun disable() {
        if (mongoClient != null) {
            mongoClient!!.close()
        }
    }

    fun getCollection(name : String) : MongoCollection<Document> {
        return mongoDatabase!!.getCollection(name)
    }
}