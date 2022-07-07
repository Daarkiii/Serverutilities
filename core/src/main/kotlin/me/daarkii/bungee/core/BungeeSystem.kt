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

package me.daarkii.bungee.core

import me.daarkii.bungee.core.addon.AddonHandler
import me.daarkii.bungee.core.command.impl.AddonCMD
import me.daarkii.bungee.core.command.impl.GroupsCMD
import me.daarkii.bungee.core.handler.PluginHandler
import me.daarkii.bungee.core.command.impl.TestCMD
import me.daarkii.bungee.core.command.impl.group.GroupCMD
import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.config.impl.CommandFile
import me.daarkii.bungee.core.config.impl.DatabaseFile
import me.daarkii.bungee.core.config.impl.SettingFile
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.data.UserRegistry
import me.daarkii.bungee.core.handler.group.GroupHandler
import me.daarkii.bungee.core.handler.group.MongoGroupHandler
import me.daarkii.bungee.core.handler.user.MongoUserHandler
import me.daarkii.bungee.core.handler.user.UserHandler
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.storage.MongoDB
import me.daarkii.bungee.core.storage.MySQL
import me.daarkii.bungee.core.utils.Logger
import me.daarkii.bungee.core.utils.Platform
import me.daarkii.bungee.core.utils.Settings
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

abstract class BungeeSystem(
    val logger: Logger,
    val dataFolder: File,
    val platform: Platform
    ) {

    //Manages Addons and loads them
    lateinit var addonHandler: AddonHandler
    lateinit var groupHandler: GroupHandler

    //Files
    lateinit var settingFile: Config
    lateinit var commandFile: Config
    lateinit var databaseFile: DatabaseFile

    protected fun init() {

        //Create an instance for the Settings class
        Settings()
        UserRegistry()

        //load Files
        settingFile = SettingFile(dataFolder)
        commandFile = CommandFile(dataFolder)

        //Load Messages
        Message(this.settingFile.getString("language"), this.dataFolder)

        //Load Database and connect
        databaseFile = DatabaseFile(this)

        //load Groups
        this.groupHandler.loadGroups()

        //enable Addons
        this.addonHandler = AddonHandler(this)
        this.addonHandler.loadAddons()

        //enable Commands
        this.loadCommands()
    }

    fun stop() {
        CompletableFuture.runAsync { this.groupHandler.safeGroups() }
        TimeUnit.SECONDS.sleep(1)
    }

    private fun loadCommands() {
        this.pluginHandler.registerCommand(TestCMD())
        this.pluginHandler.registerCommand(AddonCMD(this))
        this.pluginHandler.registerCommand(GroupCMD(this))
        this.pluginHandler.registerCommand(GroupsCMD(this))
    }

    val debugMode: Boolean
        get() = settingFile.getBoolean("debug")

    var mySQL: MySQL? = null

    var mongo: MongoDB? = null

    protected fun setInstance(bs: BungeeSystem) {
        instance = bs
    }

    val userHandler: UserHandler
        get() {
            return MongoUserHandler(mongo!!)
        }

    /**
     * Disables this plugin
     */
    abstract fun shutdown()

    /**
     * Gets a Console Object for a specified Platform
     * @return a CompletableFuture<Console> with the Console object
     */
    abstract val console: CompletableFuture<Console>

    /**
     * Gets a User Object for a specified Platform
     * @return a CompletableFuture<User> with the User object
     */
    abstract fun getUser(uuid: UUID) : CompletableFuture<User?>

    /**
     * Gets a OfflineUser Object for a specified Platform
     * @return a CompletableFuture<OfflineUser> with the OfflineUser object
     */
    abstract fun getOfflineUser(uuid: UUID) : CompletableFuture<OfflineUser?>

    abstract val pluginHandler: PluginHandler

    companion object {
        private var instance: BungeeSystem? = null

        @JvmStatic
        fun getInstance() : BungeeSystem {
            return instance!!
        }
    }
}