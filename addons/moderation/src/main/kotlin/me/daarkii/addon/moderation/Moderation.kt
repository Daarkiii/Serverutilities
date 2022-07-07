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

    //files
    lateinit var config: Config

    override fun onStart() {

        instance = this

        //load Files
        config = ConfigFile(this.dataFolder)

        //connect to the database
        this.connectToDatabase()
    }

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

            //Let the plugin know that it should use mysql
            Settings.instance.useMySQL = true

        } else if(databaseFile.getString("plugins.dbutilities.storage").equals("mongodb", ignoreCase = true)) {

            //Connect to Mongo
            val mongoUrl: String = if(databaseFile.getBoolean("mongo.isOnLocalhost"))
                "mongodb://" + databaseFile.getString("mongo.host") + ":" + databaseFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"
            else
                "mongodb://" + databaseFile.getString("mongo.user") + ":" + databaseFile.getString("mongo.password") + "@" + databaseFile.getString("mongo.host") + ":" + databaseFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"

            mongoDB = MongoDB(mongoUrl)
            mongoDB!!.connect(databaseFile.getString("plugins.dbutilities.database"))

            system.logger.debug("Successfully connected to a mongodb database")
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: Moderation
            private set
    }

}