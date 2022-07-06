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