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

package me.daarkii.addon.moderation.`object`

import java.util.stream.Collectors

class History(private var loadedEntries: MutableList<HistoryEntry>) {

    /**
     * Gets all entries of this history
     *
     * @return a sorted List with all entries
     */
    val entries: List<HistoryEntry>
        get() {
            loadedEntries.sortedByDescending { it.id }
            return loadedEntries
        }

    /**
     * Adds an entry to the current entries
     * Note that this will not update the database!
     */
    fun addEntry(entry: HistoryEntry) {
        if(loadedEntries.contains(entry))
            return
        loadedEntries.add(entry)
    }

    /**
     * Gets the first HistoryEntry from the list above
     *
     * @return the entry if exist or null
     */
    val first: HistoryEntry? = entries.firstOrNull()

    /**
     * Gets the last HistoryEntry from the list above
     *
     * @return the entry if exist or null
     */
    val last: HistoryEntry? = entries.lastOrNull()

    /**
     * Gets the count of all entries
     *
     * @return the size of all entries
     */
    val entryCount= entries.size

    /**
     * Filters all entries and collect every entry which has the type ban
     */
    val banEntries: List<HistoryEntry>
        get() = this.entries.stream().filter { it.type == Type.BAN }.collect(Collectors.toList())

    /**
     * Filters all entries and collect every entry which has the type mute
     */
    val muteEntries: Collection<HistoryEntry>
        get() = this.entries.stream().filter { it.type == Type.MUTE }.collect(Collectors.toList())

    /**
     * Filters all entries and collect every entry with the given reason
     *
     * @param reason the reason to filter
     * @return a collection with entries or an empty map
     */
    fun filterReasons(reason: Reason) : Collection<HistoryEntry> {
        return this.entries.stream().filter { it.reason == reason }.collect(Collectors.toList())
    }
}