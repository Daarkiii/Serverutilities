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

import me.daarkii.bungee.bukkit.impl.command.BukkitCommand
import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.handler.PluginHandler
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin

class BukkitPluginHandler(private val bukkit: JavaPlugin) : PluginHandler() {

    override fun registerCommand(command: Command) {

        val field = bukkit.server.javaClass.getDeclaredField("commandMap")

        field.isAccessible = true
        val map = field.get(bukkit.server) as CommandMap

        map.register(command.name, BukkitCommand(command))
    }

}