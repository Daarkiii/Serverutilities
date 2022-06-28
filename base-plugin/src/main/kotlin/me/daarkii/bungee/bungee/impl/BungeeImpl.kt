package me.daarkii.bungee.bungee.impl

import me.daarkii.bungee.bungee.BungeeHook
import me.daarkii.bungee.bungee.impl.`object`.BungeeConsole
import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.Platform
import net.kyori.adventure.platform.bungeecord.BungeeAudiences
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Implementation of the BungeeSystem Main Class for the BungeeCord Platform
 */
class BungeeImpl(private val bungee: BungeeHook) : BungeeSystem(
    BungeeLogger(bungee),
    bungee.dataFolder,
    Platform.BUNGEE,
) {

    private var bungeeAudiences = BungeeAudiences.create(bungee)

    init {
        setInstance(this)
        init()
        instance = this
    }

    override fun shutdown() {
        this.bungee.proxy.stop()
    }

    override fun getConsole(): CompletableFuture<Console> {
        return CompletableFuture.supplyAsync { BungeeConsole(bungeeAudiences, this.bungee.proxy.console) }
    }

    override fun getUser(uuid: UUID): CompletableFuture<User?> {
        return CompletableFuture()
    }

    override fun getOfflineUser(uuid: UUID): CompletableFuture<OfflineUser?> {
        return CompletableFuture()
    }

    fun getAdventure() : BungeeAudiences {
        return bungeeAudiences
    }

    companion object {
        @JvmStatic
        lateinit var instance: BungeeImpl
            private set
    }
}