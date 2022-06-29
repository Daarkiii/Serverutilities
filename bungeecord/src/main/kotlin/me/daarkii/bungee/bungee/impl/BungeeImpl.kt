package me.daarkii.bungee.bungee.impl

import me.daarkii.bungee.bungee.BungeeHook
import me.daarkii.bungee.bungee.impl.`object`.BungeeConsole
import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.PluginHandler
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.Platform
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import net.md_5.bungee.api.ProxyServer
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.logging.Handler

/**
 * Implementation of the BungeeSystem Main Class for the BungeeCord Platform
 */
class BungeeImpl(private val bungee: BungeeHook) : BungeeSystem(
    BungeeLogger(bungee),
    bungee.dataFolder,
    Platform.BUNGEE,
) {

    private val bungeeAudiences = BungeeAudiences.create(bungee)

    init {
        setInstance(this)
        init()
        instance = this
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
        get() = CompletableFuture.supplyAsync { BungeeConsole(bungeeAudiences, this.bungee.proxy.console) }

    override fun getUser(uuid: UUID): CompletableFuture<User?> {
        return CompletableFuture()
    }

    override fun getOfflineUser(uuid: UUID): CompletableFuture<OfflineUser?> {
        return CompletableFuture()
    }

    fun getAdventure() : BungeeAudiences {
        return bungeeAudiences
    }

    override val pluginHandler: PluginHandler
        get() = TODO()

    companion object {
        @JvmStatic
        lateinit var instance: BungeeImpl
            private set
    }
}