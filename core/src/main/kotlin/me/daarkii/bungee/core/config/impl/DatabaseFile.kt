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

package me.daarkii.bungee.core.config.impl

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.handler.group.MongoGroupHandler
import me.daarkii.bungee.core.storage.MongoDB
import me.daarkii.bungee.core.storage.MySQL
import me.daarkii.bungee.core.utils.Settings
import java.io.File

class DatabaseFile(private val bungee: BungeeSystem) : Config(File(bungee.dataFolder, "database.yml"), "database.yml") {

    /**
     * Gets called after the file is loaded
     */
    override fun afterLoad() {

        //Connect to database
        if(this.getString("plugins.dbutilities.storage").equals("mysql", ignoreCase = true)) {

            //Connect to mysql
            bungee.mySQL = MySQL(this, "dbutilities")
            bungee.logger.debug("Successfully connected to a mysql database")

            //Let the plugin know that it should use mysql
            Settings.instance.useMySQL = true

        } else if(this.getString("plugins.dbutilities.storage").equals("mongodb", ignoreCase = true)) {

            //Connect to Mongo
            val mongoUrl: String = if(this.getBoolean("mongo.isOnLocalhost"))
                "mongodb://" + this.getString("mongo.host") + ":" + this.getString("mongo.port") + "/?maxPoolSize=20&w=majority"
            else
                "mongodb://" + this.getString("mongo.user") + ":" + this.getString("mongo.password") + "@" + this.getString("mongo.host") + ":" + this.getString("mongo.port") + "/?maxPoolSize=20&w=majority"

            bungee.mongo = MongoDB(mongoUrl)
            bungee.mongo!!.connect(this.getString("plugins.dbutilities.database"))

            bungee.logger.debug("Successfully connected to a mongodb database")

            //Let the plugin know that it should use mongodb
            Settings.instance.useMongo = true
            bungee.groupHandler = MongoGroupHandler(bungee.mongo!!)

        } else {
            bungee.logger.sendError("You have not selected a storage provider!")
            bungee.logger.sendError("Shutting down...")

            bungee.shutdown()
            return
        }

    }

}