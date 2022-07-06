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
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.Platform
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.CompletableFuture

class BukkitImpl(private val bukkit: JavaPlugin) : BungeeSystem(
    BukkitLogger(bukkit),
    bukkit.dataFolder,
    Platform.BUKKIT
) {

    override val pluginHandler = BukkitPluginHandler(bukkit)

    val adventure = BukkitAudiences.create(bukkit)

    init {
        setInstance(this)
        init()
        instance = this
    }

    /**
     * Disables this plugin
     */
    override fun shutdown() {
        Bukkit.getPluginManager().disablePlugin(bukkit)
    }

    /**
     * Gets a Console Object for a specified Platform
     * @return a CompletableFuture<Console> with the Console object
     */
    override val console: CompletableFuture<Console>
        get() = CompletableFuture()

    /**
     * Gets a Console Object for a specified Platform
     * @return a CompletableFuture<Console> with the Console object
     */
    override fun getUser(uuid: UUID): CompletableFuture<User?> {
        TODO("Not yet implemented")
    }

    override fun getOfflineUser(uuid: UUID): CompletableFuture<OfflineUser?> {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        lateinit var instance: BukkitImpl
            private set
    }
}