package me.daarkii.bungee.core

import me.daarkii.bungee.core.addon.AddonHandler
import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.config.impl.SettingFile
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.storage.MongoDB
import me.daarkii.bungee.core.storage.MySQL
import me.daarkii.bungee.core.utils.Logger
import me.daarkii.bungee.core.utils.Platform
import java.io.File
import java.util.*
import java.util.concurrent.CompletableFuture

abstract class BungeeSystem(
    private val logger: Logger,
    private val dataFolder: File,
    private val platform: Platform
    ) {

    //Storage
    private lateinit var mySQL: MySQL
    private lateinit var mongoDB: MongoDB
    private lateinit var addonHandler: AddonHandler

    //Files
    private lateinit var settingFile: Config

    protected fun init() {

        //load Files
        settingFile = SettingFile(dataFolder)

        //Connect to database
        if(settingFile.getString("storage").equals("mysql", ignoreCase = true)) {

            //Connect to mysql
            this.mySQL = MySQL(settingFile)
            this.logger.debug("Successfully connected to a mysql database")

        } else if(settingFile.getString("storage").equals("mongodb", ignoreCase = true)) {

            //Connect to Mongo
            val mongoUrl: String = if(settingFile.getBoolean("mongo.isOnLocalhost"))
                "mongodb://" + settingFile.getString("mongo.host") + ":" + settingFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"
            else
                "mongodb://" + settingFile.getString("mongo.user") + ":" + settingFile.getString("mongo.password") + "@" + settingFile.getString("mongo.host") + ":" + settingFile.getString("mongo.port") + "/?maxPoolSize=20&w=majority"

            this.mongoDB = MongoDB(mongoUrl)
            this.mongoDB.connect(settingFile.getString("mongo.database"))

            this.logger.debug("Successfully connected to a mongodb database")
        } else {
            this.logger.sendError("You have not selected a storage provider!")
            this.logger.sendError("Shutting down...")

            this.shutdown()
            return
        }

        //enable Addons
        this.addonHandler = AddonHandler(this)
        this.addonHandler.loadAddons()

    }

    fun isInDebugMode() : Boolean {
        return settingFile.getBoolean("debug")
    }

    fun getMySQL() : MySQL {
        return mySQL
    }

    fun getMongo() : MongoDB {
        return mongoDB
    }

    fun getLogger() : Logger {
        return logger
    }

    fun getDataFolder() : File {
        return dataFolder
    }

    fun getPlatform() : Platform {
        return platform
    }

    protected fun setInstance(bs: BungeeSystem) {
        instance = bs
    }

    protected abstract fun shutdown()

    abstract fun getConsole() : CompletableFuture<Console>

    abstract fun getUser(uuid: UUID) : CompletableFuture<User>

    abstract fun getOfflineUser(uuid: UUID) : CompletableFuture<OfflineUser>

    companion object {
        private var instance: BungeeSystem? = null

        fun getInstance() : BungeeSystem {
            return instance!!
        }
    }
}