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
import me.daarkii.bungee.core.handler.PluginHandler
import me.daarkii.bungee.core.command.impl.TestCMD
import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.config.impl.CommandFile
import me.daarkii.bungee.core.config.impl.SettingFile
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.data.UserRegistry
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

abstract class BungeeSystem(
    val logger: Logger,
    val dataFolder: File,
    val platform: Platform
    ) {

    //Manages Addons and loads them
    lateinit var addonHandler: AddonHandler

    //Files
    lateinit var settingFile: Config
    lateinit var commandFile: Config

    protected fun init() {

        //Create an instance for the Settings class
        Settings()
        UserRegistry()

        //load Files
        settingFile = SettingFile(dataFolder)
        commandFile = CommandFile(dataFolder)

        //Load Messages
        Message(this.settingFile.getString("language"), this.dataFolder)

        //Connect to database
        if(settingFile.getString("storage").equals("mysql", ignoreCase = true)) {

            //Connect to mysql
            this.mySQL = MySQL(settingFile)
            this.logger.debug("Successfully connected to a mysql database")

            //Let the plugin know that it should use mysql
            Settings.instance.useMySQL = true

        } else if(settingFile.getString("storage").equals("mongodb", ignoreCase = true)) {

            //Connect to Mongo
            val mongoUrl: String = if(settingFile.getBoolean("mongo.isOnLocalhost"))
                "mongodb://" + settingFile.getString("mongo.host") + ":" + settingFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"
            else
                "mongodb://" + settingFile.getString("mongo.user") + ":" + settingFile.getString("mongo.password") + "@" + settingFile.getString("mongo.host") + ":" + settingFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"

            this.mongo = MongoDB(mongoUrl)
            this.mongo!!.connect(settingFile.getString("mongo.database"))

            this.logger.debug("Successfully connected to a mongodb database")

            //Let the plugin know that it should use mongodb
            Settings.instance.useMongo = true

        } else {
            this.logger.sendError("You have not selected a storage provider!")
            this.logger.sendError("Shutting down...")

            this.shutdown()
            return
        }

        //enable Addons
        this.addonHandler = AddonHandler(this)
        this.addonHandler.loadAddons()

        //enable Commands
        this.loadCommands()
    }

    private fun loadCommands() {
        this.pluginHandler.registerCommand(TestCMD())
        this.pluginHandler.registerCommand(AddonCMD(this))
    }

    val debugMode: Boolean
        get() = settingFile.getBoolean("debug")

    private var mySQL: MySQL? = null

    private var mongo: MongoDB? = null

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
    protected abstract fun shutdown()

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