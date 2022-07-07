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

import me.daarkii.bungee.core.utils.PlaceHolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

interface CommandSender {

    /**
     * Gets the name of the command sender
     * @return the name
     */
    val name: String

    /**
     * Gets the colored name of the command sender
     * @return the colored name
     */
    val displayName: String

    /**
     * Checks if the command sender has the given permission
     * @param permission the permission to check
     * @return true if the command sender has the permission
     */
    fun hasPermission(permission: String) : Boolean

    /**
     * Sends the given message to the command sender
     * @param msg the message to send
     */
    fun sendMessage(msg: String)

    /**
     * Sends the given message to the command sender
     * @param msg the message to send
     * @param placeHolder which should be replaced
     */
    fun sendMessage(msg: String, vararg placeHolder: PlaceHolder)

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    fun sendMessage(component: TextComponent)

    /**
     * Sends the given component  to the command sender
     * @param component the component to send
     */
    fun sendMessage(component: Component)

    /**
     * Sends the given MiniMessage String  to the command sender
     * @param miniMsg the not deserialized MiniMessage String
     */
    fun sendMiniMessage(miniMsg: String)

    /**
     * Executes the given command
     */
    fun executeCommand(cmd: String)

}