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

package me.daarkii.bungee.bukkit.impl

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.utils.Logger
import org.bukkit.plugin.java.JavaPlugin

class BukkitLogger(private val bukkit: JavaPlugin) : Logger {

    /**
     * Sends a message to the Console
     * @param msg the Message to send
     */
    override fun sendConsoleMessage(msg: String) {
        bukkit.server.consoleSender.sendMessage(msg)
    }

    /**
     * Sends a message to every online player
     * @param msg the Message to send
     */
    override fun sendMessage(msg: String) {
        bukkit.server.onlinePlayers.forEach { player -> player.sendMessage(msg) }
    }

    /**
     * Sends a message to every online player with the given permission
     * @param msg the Message to send
     * @param permission the permission which the player needs
     */
    override fun sendMessage(msg: String, permission: String) {
        bukkit.server.onlinePlayers.forEach { player ->
            if(player.hasPermission(permission))
                player.sendMessage(msg)
        }
    }

    /**
     * Sends a message to the Console if the Server is in the debug mode
     * @param msg the Message to send
     */
    override fun debug(msg: String) {
        if(!BungeeSystem.getInstance().debugMode)
            return

        this.sendConsoleMessage("§a§lBungee-DEBUG §8» §6$msg")
    }

    /**
     * Sends a message to the Console when a small problem occurred
     * @param msg the Message to send
     */
    override fun sendWarning(msg: String) {
        this.sendConsoleMessage("§6§lBungee-WARNING §8» §6$msg")
    }

    /**
     * Sends a message to the console when the server has detected an error
     * @param msg the Message to send
     */
    override fun sendError(msg: String) {
        this.sendConsoleMessage("§6§l§c§lBungee-ERROR §8» §c$msg §8» §6$msg")
    }

}