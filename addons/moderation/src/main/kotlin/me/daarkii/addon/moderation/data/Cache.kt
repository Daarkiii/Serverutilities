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

package me.daarkii.addon.moderation.data

import me.daarkii.addon.moderation.`object`.History
import java.util.concurrent.ConcurrentHashMap

class Cache {

    init {
        instance = this
    }

    private val userHistories: MutableMap<Long, History> = ConcurrentHashMap()

    fun setHistory(userID: Long, history: History) {
        this.userHistories[userID] = history
    }

    fun removeHistory(userID: Long) {
        this.userHistories.remove(userID)
    }

    fun getHistory(userID: Long) : History? {
       return this.userHistories[userID]
    }

    fun hasHistory(userID: Long) : Boolean {
        return this.userHistories.containsKey(userID)
    }

    val histories = userHistories.values

    companion object {
        @JvmStatic
        lateinit var instance: Cache
            private set
    }

}