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

package me.daarkii.bungee.core.utils

interface Logger {

    /**
     * Sends a message to the Console
     * @param msg the Message to send
     */
    fun sendConsoleMessage(msg: String)

    /**
     * Sends a message to every online player
     * @param msg the Message to send
     */
    fun sendMessage(msg: String)

    /**
     * Sends a message to every online player with the given permission
     * @param msg the Message to send
     * @param permission the permission which the player needs
     */
    fun sendMessage(msg: String, permission: String)

    /**
     * Sends a message to the Console if the Server is in the debug mode
     * @param msg the Message to send
     */
    fun debug(msg: String)

    /**
     * Sends a message to the Console when a small problem occurred
     * @param msg the Message to send
     */
    fun sendWarning(msg: String)

    /**
     * Sends a message to the console when the server has detected an error
     * @param msg the Message to send
     */
    fun sendError(msg: String)

}