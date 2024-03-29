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

package me.daarkii.bungee.bungee.impl.`object`.user.offline

import de.dytanic.cloudnet.driver.CloudNetDriver
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeOfflineUser
import java.util.*

class CloudNetOfflineUser(
    override val id: Long,
    override val uuid: UUID,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long,
    override val name: String,
    private val groupID: Int
) : BungeeOfflineUser(id, uuid, firstJoin, lastJoin, onlineTime, name, groupID) {

    override val displayName: String
        get() {
            val permissionUser = CloudNetDriver.getInstance().permissionManagement.getUser(uuid) ?: return name
            return permissionUser.name
        }

}