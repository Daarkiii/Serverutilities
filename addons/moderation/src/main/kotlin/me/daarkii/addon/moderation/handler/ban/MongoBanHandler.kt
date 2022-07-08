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
import me.daarkii.addon.moderation.handler.BanHandler
import me.daarkii.bungee.core.storage.MongoDB
import java.util.concurrent.CompletableFuture

class MongoBanHandler(mongo: MongoDB) : BanHandler {

    /**
     * document style:
     * 1 = user id (id)
     * 2 = last ban entry id or 0 (ban)
     * 3 = last mute entry id or null (mute)
     */
    private val collection = mongo.getCollection("moderation_bans")

    /**
     * Checks if a User is banned and unban him if the ban is expired
     *
     * @param id from the User object
     * @return true if he has an active ban
     */
    override fun hasBan(id: Long): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            this.collection.find(Filters.eq("id", id)).first()?.getInteger("ban") != 0
        }
    }

    /**
     * Unban a User without marking the ban as manual unbanned
     *
     * @param id from the User object
     */
    override fun unban(id: Long): CompletableFuture<Void> {
        TODO("Not yet implemented")
    }

    /**
     * Unban a User and marking the ban as resolved if the value is true
     * @param shouldMark should be true if the ban was a false ban
     *
     * @param id from the User object
     */
    override fun unban(id: Long, shouldMark: Boolean): CompletableFuture<Void> {
        TODO("Not yet implemented")
    }
}