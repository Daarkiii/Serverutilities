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

package me.daarkii.bungee.core.data

import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.TripleMap
import java.util.UUID

class UserRegistry {

    init {
        instance = this
    }

    private val users: TripleMap<UUID, Long, User> = TripleMap()
    private val offlineUsers: TripleMap<UUID, Long, OfflineUser> = TripleMap()

    /*
    Online user
     */

    fun getUser(uuid: UUID) : User? {
        return users.get(uuid)
    }

    fun getUser(id: Long) : User? {
        return users.get(id)
    }

    fun createUser(user: User) {
        this.users.insert(user.uuid, user.id, user)
    }

    /*
    Offline user
     */

    fun getOfflineUser(uuid: UUID) : OfflineUser? {
        return offlineUsers.get(uuid)
    }

    fun getOfflineUser(id: Long) : OfflineUser? {
        return offlineUsers.get(id)
    }

    fun createOfflineUser(user: OfflineUser) {
        this.offlineUsers.insert(user.uuid, user.id, user)
    }

    companion object {
        @JvmStatic
        lateinit var instance: UserRegistry
            private set
    }
}