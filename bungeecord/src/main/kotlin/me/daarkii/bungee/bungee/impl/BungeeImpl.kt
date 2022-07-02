package me.daarkii.bungee.bungee.impl

import me.daarkii.bungee.bungee.BungeeHook
import me.daarkii.bungee.bungee.impl.`object`.BungeeConsole
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeOfflineUser
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeUser
import me.daarkii.bungee.bungee.listener.PlayerListener
import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.data.UserRegistry
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.Platform
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

            if(bungee.proxy.getPlayer(uuid) == null)
                return@supplyAsync null

            val id = userHandler.getID(uuid)
            var user: User? = null

            userHandler.loadData(id).thenAccept { data ->
                user = BungeeUser(id, uuid, this, data[3] as Long, data[4] as Long, data[5] as Long)
            }

            while (user == null) {
                TimeUnit.MILLISECONDS.sleep(100)
            }

            UserRegistry.instance.createUser(user!!)
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
            var user: OfflineUser? = null

            userHandler.loadData(id).thenAccept { data ->
                user = BungeeOfflineUser(id, uuid, data[3] as Long, data[4] as Long, data[5] as Long)
            }

            while (user == null) {
                TimeUnit.MILLISECONDS.sleep(100)
            }

            UserRegistry.instance.createOfflineUser(user!!)
            user
        }
    }

    companion object {
        @JvmStatic
        lateinit var instance: BungeeImpl
            private set
    }
}