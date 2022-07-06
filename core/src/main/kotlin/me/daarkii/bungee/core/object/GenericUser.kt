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

package me.daarkii.bungee.core.`object`

import java.util.UUID

interface GenericUser {

    /**
     * Gets the custom id of the user
     * @return the id
     */
    val id: Long

    /**
     * Gets the name of the offlineUser
     * @return the last known name of the User
     */
    val name: String

    /**
     * Gets the colored name of the offlineUser
     * @return the colored name or the normal name if the server is not using CloudNet or the player is online
     */
    val displayName: String

    /**
     * Gets the uuid of the user
     * @return the uuid
     */
    val uuid: UUID

    /**
     * Checks if the User is online
     * @return true if he is online
     */
    val isOnline: Boolean

    /**
     * Gets the timestamp where the user joined the first time
     */
    val firstJoin: Long

    /**
     * Gets the timestamp where the user joined the last time
     */
    val lastJoin: Long

    /**
     * Gets the online-time from the user in milliseconds
     */
    var onlineTime: Long
}