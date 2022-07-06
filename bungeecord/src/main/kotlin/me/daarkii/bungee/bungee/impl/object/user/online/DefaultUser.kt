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

package me.daarkii.bungee.bungee.impl.`object`.user.online

import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeUser
import net.md_5.bungee.api.ProxyServer
import java.util.*

class DefaultUser(
    id: Long,
    uuid: UUID,
    proxy: BungeeImpl,
    firstJoin: Long,
    lastJoin: Long,
    onlineTime: Long
) : BungeeUser(id, uuid, proxy, firstJoin, lastJoin, onlineTime) {

    override val displayName: String = ProxyServer.getInstance().getPlayer(uuid).displayName

    override fun connect(server: String) {

        val player = ProxyServer.getInstance().getPlayer(uuid)
        val serverInfo = ProxyServer.getInstance().getServerInfo(server)

        if(player != null && serverInfo != null)
            player.connect(serverInfo)
    }
}