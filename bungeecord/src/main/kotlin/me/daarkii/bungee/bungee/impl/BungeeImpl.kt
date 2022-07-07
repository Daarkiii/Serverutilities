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

package me.daarkii.bungee.bungee.impl

import me.daarkii.bungee.bungee.BungeeHook
import me.daarkii.bungee.bungee.impl.`object`.BungeeConsole
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeOfflineUser
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeUser
import me.daarkii.bungee.bungee.impl.`object`.user.offline.CloudNetOfflineUser
import me.daarkii.bungee.bungee.impl.`object`.user.online.CloudNetUser
import me.daarkii.bungee.bungee.impl.`object`.user.online.DefaultUser
import me.daarkii.bungee.bungee.listener.PlayerListener
import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.data.UserRegistry
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.Platform
import me.daarkii.bungee.core.utils.Settings
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * Implementation of the BungeeSystem Main Class for the BungeeCord Platform
 */
class BungeeImpl(private val bungee: BungeeHook) : BungeeSystem(
    BungeeLogger(bungee),
    bungee.dataFolder,
    Platform.BUNGEE,
) {

    override val pluginHandler = BungeePluginHandler(bungee)
    val adventure = BungeeAudiences.create(bungee)

    init {
        setInstance(this)
        init()
        instance = this

        this.bungee.proxy.pluginManager.registerListener(bungee, PlayerListener(this))
    }

    override fun shutdown() {

        //In BungeeCord there is no way to disable the plugin, so we unregister all Handlers, Listeners and Commands
        this.bungee.onDisable()

        this.bungee.logger.handlers.forEach { handler -> handler.close() }

        this.bungee.proxy.pluginManager.unregisterCommands(bungee)
        this.bungee.proxy.pluginManager.unregisterListeners(bungee)

        this.bungee.proxy.scheduler.cancel(bungee)
    }

    override val console: CompletableFuture<Console>
        get() = CompletableFuture.supplyAsync { BungeeConsole(adventure, this.bungee.proxy.console) }

    override fun getUser(uuid: UUID): CompletableFuture<User?> {
        return CompletableFuture.supplyAsync{

            val loaded = UserRegistry.instance.getUser(uuid)

            if(loaded != null)
                return@supplyAsync loaded

            if(!userHandler.isExist(uuid))
                return@supplyAsync null

            val id = userHandler.getID(uuid)
            val data = userHandler.loadData(id).join()

            val user: User = if(Settings.instance.isCloudNetActive)
                CloudNetUser(id, uuid, this, data[3] as Long, data[4] as Long, data[5] as Long)
            else
                DefaultUser(id, uuid, this, data[3] as Long, data[4] as Long, data[5] as Long)

            UserRegistry.instance.createUser(user)
            user
        }
    }

    override fun getOfflineUser(uuid: UUID): CompletableFuture<OfflineUser?> {
        return CompletableFuture.supplyAsync{

            val loaded = UserRegistry.instance.getOfflineUser(uuid)

            if(loaded != null)
                return@supplyAsync loaded

            if(!userHandler.isExist(uuid))
                return@supplyAsync null

            val id = userHandler.getID(uuid)
            val data = userHandler.loadData(id).join()
            val user: OfflineUser = CloudNetOfflineUser(id, uuid, data[3] as Long, data[4] as Long, data[5] as Long, data[2] as String, data[6] as Int)

            UserRegistry.instance.createOfflineUser(user)
            user
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: BungeeImpl
            private set
    }
}