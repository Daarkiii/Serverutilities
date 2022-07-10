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

package me.daarkii.addon.moderation

import me.daarkii.addon.moderation.config.ConfigFile
import me.daarkii.addon.moderation.config.MessageFile
import me.daarkii.addon.moderation.config.ReasonFile
import me.daarkii.addon.moderation.handler.BanHandler
import me.daarkii.addon.moderation.handler.Helper
import me.daarkii.addon.moderation.handler.HistoryHandler
import me.daarkii.addon.moderation.handler.ban.MongoBanHandler
import me.daarkii.addon.moderation.handler.ban.MySQLBanHandler
import me.daarkii.addon.moderation.handler.history.MongoHistoryHandler
import me.daarkii.bungee.core.addon.Addon
import me.daarkii.bungee.core.addon.AddonInfo
import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.storage.MongoDB
import me.daarkii.bungee.core.storage.MySQL

class Moderation(addonInfo: AddonInfo) : Addon(addonInfo) {

    //storage
    var mysql: MySQL? = null
    var mongoDB: MongoDB? = null

    //handler
    lateinit var helper: Helper
    lateinit var banHandler: BanHandler
    lateinit var historyHandler: HistoryHandler

    //files
    lateinit var config: Config
    lateinit var reasonFile: Config
    lateinit var messages: Config

    override fun onStart() {

        instance = this

        //register helper
        this.helper = Helper()

        //load Files
        config = ConfigFile(this.dataFolder)
        reasonFile = ReasonFile(this)
        messages = MessageFile(dataFolder, config.getString("language"))

        //update files and connect to the database
        this.updateFiles()
        this.connectToDatabase()
    }

    /**
     * This Addon uses the command and database file of the main plugin
     * Default the values for this plugin are not included, so we need to set them
     */
    private fun updateFiles() {

        //Update database file
        val databaseFile = this.system.databaseFile
        val databaseConfiguration = databaseFile.configuration //ignore the null safety of the default getter

        if(!databaseConfiguration.contains("plugins.moderation.storage"))
            databaseConfiguration.set("plugins.moderation.storage", "mysql")

        if(!databaseConfiguration.contains("plugins.moderation.database"))
            databaseConfiguration.set("plugins.moderation.database", "moderation")

        //Saves the new values
        databaseFile.save()

        //Update command file
        val commandFile = this.system.commandFile
        val commandConfiguration = commandFile.configuration

        if(!commandConfiguration.contains("ban")) {
            commandConfiguration.set("ban.name", "ban")
            commandConfiguration.set("ban.permission", "bungee.ban")
            commandConfiguration.set("ban.overridepermission", "bungee.ban.override")
            commandConfiguration.set("ban.aliases", listOf("pun", "punish"))
        }

        //Saves the new values
        commandConfiguration.save()
    }

    /**
     * Sets the database settings for this plugin in the database file and
     * loads them out and connect to the selected database
     */
    private fun connectToDatabase() {

        val databaseFile = this.system.databaseFile

        //Connect to database
        if(databaseFile.getString("plugins.dbutilities.storage").equals("mysql", ignoreCase = true)) {

            //Connect to mysql
            mysql = MySQL(databaseFile, "moderation")
            system.logger.debug("Successfully connected to a mysql database")

            //register handlers
            this.banHandler = MySQLBanHandler()

        } else if(databaseFile.getString("plugins.dbutilities.storage").equals("mongodb", ignoreCase = true)) {

            //Connect to Mongo
            val mongoUrl: String = if(databaseFile.getBoolean("mongo.isOnLocalhost"))
                "mongodb://" + databaseFile.getString("mongo.host") + ":" + databaseFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"
            else
                "mongodb://" + databaseFile.getString("mongo.user") + ":" + databaseFile.getString("mongo.password") + "@" + databaseFile.getString("mongo.host") + ":" + databaseFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"

            mongoDB = MongoDB(mongoUrl)
            mongoDB!!.connect(databaseFile.getString("plugins.dbutilities.database"))

            system.logger.debug("Successfully connected to a mongodb database")

            //register handlers
            this.banHandler = MongoBanHandler(this)
            this.historyHandler = MongoHistoryHandler(mongoDB!!)
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: Moderation
            private set
    }

}