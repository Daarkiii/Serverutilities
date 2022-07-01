package me.daarkii.bungee.core

import me.daarkii.bungee.core.addon.AddonHandler
import me.daarkii.bungee.core.command.PluginHandler
import me.daarkii.bungee.core.command.impl.TestCMD
import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.config.impl.SettingFile
import me.daarkii.bungee.core.config.impl.messages.Message
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
    private lateinit var addonHandler: AddonHandler

    //Files
    private lateinit var settingFile: Config

    protected fun init() {

        //Create an instance for the Settings class
        Settings()

        //load Files
        settingFile = SettingFile(dataFolder)

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
        this.pluginHandler.registerCommand(TestCMD())
    }

    val debugMode: Boolean
        get() = settingFile.getBoolean("debug")

    var mySQL: MySQL? = null

    var mongo: MongoDB? = null

    protected fun setInstance(bs: BungeeSystem) {
        instance = bs
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