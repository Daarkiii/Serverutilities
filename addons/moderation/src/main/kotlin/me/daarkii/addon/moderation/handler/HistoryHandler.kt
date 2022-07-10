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

import me.daarkii.addon.moderation.`object`.History
import me.daarkii.addon.moderation.`object`.HistoryEntry
import java.util.concurrent.CompletableFuture

interface HistoryHandler {

    /**
     * Creates a ban in the database and updates the cache if needed
     *
     * @param entry the entry
     */
    fun saveEntry(entry: HistoryEntry) : CompletableFuture<Void>

    /**
     * Gets a History object with given entries of the User
     *
     * @param userID the id of the User
     * @return the History Object
     */
    fun getHistory(userID: Long) : CompletableFuture<History>

    /**
     * Gets a HistoryEntry with the given id out of the database
     *
     * @param id the id of the entry
     * @return the entry if exists
     */
    fun getEntry(id: Int) : CompletableFuture<HistoryEntry?>

    /**
     * Gets a HistoryEntry with the given id out of the database if the Entry is not cached
     *
     * @param userID the id of the owner
     * @param id the id of the entry
     * @return the entry if exists
     */
    fun getEntry(userID: Long, id: Int) : CompletableFuture<HistoryEntry?>

    /**
     * Gets the next available id for a HistoryEntry
     *
     * @return the next id
     */
    val nextID: Int

}