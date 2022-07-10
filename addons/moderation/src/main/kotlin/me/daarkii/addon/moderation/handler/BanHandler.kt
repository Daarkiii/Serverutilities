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

package me.daarkii.addon.moderation.handler

import me.daarkii.addon.moderation.`object`.HistoryEntry
import me.daarkii.addon.moderation.`object`.Reason
import java.util.concurrent.CompletableFuture

interface BanHandler {

    /**
     * Checks if a User is banned and unban him if the ban is expired
     *
     * @param id from the User object
     * @return true if he has an active ban
     */
    fun hasBan(id: Long) : CompletableFuture<Boolean>

    /**
     * Checks if a User is muted and unmute him if the ban is expired
     *
     * @param id from the User object
     * @return true if he has an active ban
     */
    fun hasMute(id: Long) : CompletableFuture<Boolean>

    /**
     * Registers a ban in the database
     *
     * @param entry the entry for the ban
     */
    fun create(entry: HistoryEntry) : CompletableFuture<Void>

    /**
     * Unban a User without marking the ban as manual unbanned
     *
     * @param id from the User object
     */
    fun unban(id: Long) : CompletableFuture<Void>

    /**
     * Unmute a User without marking the ban as manual unbanned
     *
     * @param id from the User object
     */
    fun unmute(id: Long) : CompletableFuture<Void>

    /**
     * Gets the id of the active ban entry of the User
     *
     * @param id of the User
     * @return the id of the entry or 0
     */
    fun getActiveBanEntryID(id: Long) : CompletableFuture<Int>

    /**
     * Gets the id of the active mute entry of the User
     *
     * @param id of the User
     * @return the id of the entry or 0
     */
    fun getActiveMuteEntryID(id: Long) : CompletableFuture<Int>

}