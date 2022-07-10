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

package me.daarkii.addon.moderation.handler.history

import com.mongodb.client.model.Filters
import me.daarkii.addon.moderation.Moderation
import me.daarkii.addon.moderation.data.Cache
import me.daarkii.addon.moderation.handler.HistoryHandler
import me.daarkii.addon.moderation.`object`.History
import me.daarkii.addon.moderation.`object`.HistoryEntry
import me.daarkii.bungee.core.storage.MongoDB
import org.bson.Document
import java.util.LinkedList
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

class MongoHistoryHandler(mongo: MongoDB) : HistoryHandler {

    /**
     * There is a document for every existing HistoryEntry
     *
     * document style:
     * 1 = entry id
     * 2 = owner id
     * 3 = mod id
     * 4 = count for the reason
     * 5 = reason id
     * 6 = start timestamp
     * 7 = end timestamp
     * 8 = manual unbanned
     */
    private val collection = mongo.getCollection("moderation_history")

    /**
     * Gets the next available id for a HistoryEntry
     *
     * @return the next id
     */
    override val nextID: Int = this.collection.countDocuments().toInt() + 1

    /**
     * Creates a ban in the database and updates the cache if needed
     *
     * @param entry the entry
     */
    override fun saveEntry(entry: HistoryEntry) : CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            val filter = Filters.eq("id", entry.id)
            val document = Document()
                .append("id", entry.id)
                .append("owner", entry.owner)
                .append("mod", entry.mod)
                .append("count", entry.count)
                .append("reason", entry.reason?.id)
                .append("start", entry.start)
                .append("end", entry.end)
                .append("unbanned", entry.unBanned)

            //Update in database
            if (this.collection.find(filter).first() != null)
                this.collection.updateOne(filter, document)
            else
                this.collection.insertOne(document)

            //Update the cache
            Cache.instance.getHistory(entry.owner)?.addEntry(entry)
        }
    }

    /**
     * Gets a History object with given entries of the User
     *
     * @param userID the id of the User
     * @return the History Object
     */
    override fun getHistory(userID: Long): CompletableFuture<History> {
        return CompletableFuture.supplyAsync {

            if (Cache.instance.hasHistory(userID))
                return@supplyAsync Cache.instance.getHistory(userID)!!

            val entries: MutableList<HistoryEntry> = LinkedList()

            this.collection.find(Filters.eq("owner", userID)).iterator().forEach { document ->

                entries.add(
                    HistoryEntry(
                        document.getInteger("id"),
                        document.getLong("owner"),
                        document.getLong("mod"),
                        document.getInteger("count"),
                        Moderation.instance.helper.getReason(document.getInteger("reason")),
                        document.getLong("start"),
                        document.getLong("end"),
                        document.getBoolean("unbanned")
                    )
                )

            }

            History(entries)
        }
    }

    /**
     * Gets a HistoryEntry with the given id out of the database
     *
     * @param id the id of the entry
     * @return the entry if exists
     */
    override fun getEntry(id: Int): CompletableFuture<HistoryEntry?> {
        return CompletableFuture.supplyAsync {

            val filter = Filters.eq("id", id)
            val document = this.collection.find(filter).first() ?: return@supplyAsync null

            HistoryEntry(
                id,
                document.getLong("owner"),
                document.getLong("mod"),
                document.getInteger("count"),
                Moderation.instance.helper.getReason(document.getInteger("reason")),
                document.getLong("start"),
                document.getLong("end"),
                document.getBoolean("unbanned")
            )
        }
    }

    /**
     * Gets a HistoryEntry with the given id out of the database if the Entry is not cached
     *
     * @param userID the id of the owner
     * @param id the id of the entry
     * @return the entry if exists
     */
    override fun getEntry(userID: Long, id: Int): CompletableFuture<HistoryEntry?> {
        return CompletableFuture.supplyAsync {

            val history = Cache.instance.getHistory(userID)

            if(history != null) {
                val entry = history.entries.stream().filter { it.id == id }.collect(Collectors.toList()).firstOrNull()
                if(entry == null)
                    return@supplyAsync entry
            }

            val filter = Filters.eq("id", id)
            val document = this.collection.find(filter).first() ?: return@supplyAsync null

            HistoryEntry(
                id,
                document.getLong("owner"),
                document.getLong("mod"),
                document.getInteger("count"),
                Moderation.instance.helper.getReason(document.getInteger("reason")),
                document.getLong("start"),
                document.getLong("end"),
                document.getBoolean("unbanned")
            )
        }
    }
}