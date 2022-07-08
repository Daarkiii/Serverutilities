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
import me.daarkii.addon.moderation.config.ReasonFile
import me.daarkii.addon.moderation.handler.BanHandler
import me.daarkii.addon.moderation.handler.Helper
import me.daarkii.addon.moderation.handler.MuteHandler
import me.daarkii.addon.moderation.handler.ban.MongoBanHandler
import me.daarkii.addon.moderation.handler.ban.MySQLBanHandler
import me.daarkii.addon.moderation.handler.mute.MongoMuteHandler
import me.daarkii.addon.moderation.handler.mute.SQLMuteHandler
import me.daarkii.bungee.core.addon.Addon
import me.daarkii.bungee.core.addon.AddonInfo
import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.storage.MongoDB
import me.daarkii.bungee.core.storage.MySQL
import me.daarkii.bungee.core.utils.Settings

class Moderation(addonInfo: AddonInfo) : Addon(addonInfo) {

    //storage
    var mysql: MySQL? = null
    var mongoDB: MongoDB? = null

    //handler
    lateinit var helper: Helper
    lateinit var banHandler: BanHandler
    lateinit var muteHandler: MuteHandler

    //files
    lateinit var config: Config
    lateinit var reasonFile: Config

    override fun onStart() {

        instance = this

        //register helper
        this.helper = Helper()

        //load Files
        config = ConfigFile(this.dataFolder)
        reasonFile = ReasonFile(this)

        //connect to the database
        this.connectToDatabase()
    }

    /**
     * Sets the database settings for this plugin in the database file and
     * loads them out and connect to the selected database
     */
    private fun connectToDatabase() {

        val databaseFile = this.system.databaseFile
        val configuration = databaseFile.configuration //ignore the null safety of the default getter

        if(!configuration.contains("plugins.moderation.storage"))
            configuration.set("plugins.moderation.storage", "mysql")

        if(!configuration.contains("plugins.moderation.database"))
            configuration.set("plugins.moderation.database", "moderation")

        //Saves the new values
        databaseFile.save()

        //Connect to database
        if(databaseFile.getString("plugins.dbutilities.storage").equals("mysql", ignoreCase = true)) {

            //Connect to mysql
            mysql = MySQL(databaseFile, "moderation")
            system.logger.debug("Successfully connected to a mysql database")

            //register handlers
            this.banHandler = MySQLBanHandler()
            this.muteHandler = SQLMuteHandler()

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
            this.banHandler = MongoBanHandler(mongoDB!!)
            this.muteHandler = MongoMuteHandler()
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: Moderation
            private set
    }

}