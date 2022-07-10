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

package me.daarkii.addon.moderation.handler.ban

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import me.daarkii.addon.moderation.Moderation
import me.daarkii.addon.moderation.handler.BanHandler
import me.daarkii.addon.moderation.`object`.HistoryEntry
import me.daarkii.addon.moderation.`object`.Type
import me.daarkii.bungee.core.storage.MongoDB
import org.bson.Document
import java.util.concurrent.CompletableFuture

class MongoBanHandler(private val moderation: Moderation) : BanHandler {

    /**
     * document style:
     * 1 = user id (id)
     * 2 = last ban entry id or 0 (ban)
     * 3 = last mute entry id or null (mute)
     */
    private val collection = moderation.mongoDB!!.getCollection("moderation_bans")
    private val historyHandler = moderation.historyHandler

    /**
     * Checks if a User is banned and unban him if the ban is expired
     *
     * @param id from the User object
     * @return true if he has an active ban
     */
    override fun hasBan(id: Long): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {

            val filter = Filters.eq("id", id)
            val document = this.collection.find(filter).first() ?: return@supplyAsync false

            val entry = this.historyHandler.getEntry(id, document.getInteger("ban")).join() ?: return@supplyAsync false

            if(entry.end <= System.currentTimeMillis()) {
                this.unban(id)
                return@supplyAsync false
            }

            true
        }
    }

    /**
     * Checks if a User is muted and unmute him if the ban is expired
     *
     * @param id from the User object
     * @return true if he has an active ban
     */
    override fun hasMute(id: Long): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {

            val filter = Filters.eq("id", id)
            val document = this.collection.find(filter).first() ?: return@supplyAsync false

            val entry = this.historyHandler.getEntry(id, document.getInteger("mute")).join() ?: return@supplyAsync false

            if(entry.end <= System.currentTimeMillis()) {
                this.unmute(id)
                return@supplyAsync false
            }

            true
        }
    }

    /**
     * Registers a ban in the database
     *
     * @param entry the entry for the ban
     */
    override fun create(entry: HistoryEntry): CompletableFuture<Void> {
        return CompletableFuture.runAsync {

            var banID = this.getActiveBanEntryID(entry.owner).join()
            var muteID = this.getActiveMuteEntryID(entry.owner).join()

            if(entry.reason?.types?.get(entry.count) == Type.BAN)
                banID = entry.id
            else
                muteID = entry.id

           val filter = Filters.eq("id", entry.owner)
            val document = Document()
                .append("id", entry.owner)
                .append("ban", banID)
                .append("mute", muteID)

            if(this.collection.find(filter).first() != null)
                this.collection.updateOne(filter, document)
            else
                this.collection.insertOne(document)
        }
    }

    /**
     * Unban a User without marking the ban as manual unbanned
     *
     * @param id from the User object
     */
    override fun unban(id: Long): CompletableFuture<Void> {
        return CompletableFuture.runAsync {

            val filter = Filters.eq("id", id)
            val update = Updates.combine(Updates.set("ban", 0))
            val options = UpdateOptions().upsert(true)

            this.collection.updateOne(filter, update, options)
        }
    }

    /**
     * Unmute a User without marking the ban as manual unbanned
     *
     * @param id from the User object
     */
    override fun unmute(id: Long): CompletableFuture<Void> {
        return CompletableFuture.runAsync {

            val filter = Filters.eq("id", id)
            val update = Updates.combine(Updates.set("mute", 0))
            val options = UpdateOptions().upsert(true)

            this.collection.updateOne(filter, update, options)
        }
    }

    /**
     * Gets the id of the active ban entry of the User
     *
     * @param id of the User
     * @return the id of the entry or 0
     */
    override fun getActiveBanEntryID(id: Long): CompletableFuture<Int> {
        return CompletableFuture.supplyAsync { this.collection.find(Filters.eq("id", id)).first()?.getInteger("ban") ?: 0 }
    }

    /**
     * Gets the id of the active mute entry of the User
     *
     * @param id of the User
     * @return the id of the entry or 0
     */
    override fun getActiveMuteEntryID(id: Long): CompletableFuture<Int> {
        return CompletableFuture.supplyAsync { this.collection.find(Filters.eq("id", id)).first()?.getInteger("mute") ?: 0 }
    }
}