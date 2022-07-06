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

package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.core.`object`.OfflineUser
import net.md_5.bungee.api.ProxyServer
import java.util.*

abstract class BungeeOfflineUser(
    override val id: Long,
    override val uuid: UUID,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long,
    override val name: String
) : OfflineUser {

    override val isOnline = ProxyServer.getInstance().getPlayer(uuid) != null

    abstract override val displayName: String

    override fun equals(other: Any?): Boolean {

        if(other == null)
            return false

        if(this === other)
            return true

        if(other !is OfflineUser)
            return false

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

}