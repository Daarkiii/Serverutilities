package me.daarkii.bungee.bukkit.impl

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.`object`.Console
import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.Platform
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.CompletableFuture

class BukkitImpl(private val bukkit: JavaPlugin) : BungeeSystem(
    BukkitLogger(bukkit),
    bukkit.dataFolder,
    Platform.BUKKIT
) {

    override val pluginHandler = BukkitPluginHandler(bukkit)

    val adventure = BukkitAudiences.create(bukkit)

    init {
        setInstance(this)
        init()
        instance = this
    }

    /**
     * Disables this plugin
     */
    override fun shutdown() {
        Bukkit.getPluginManager().disablePlugin(bukkit)
    }

    /**
     * Gets a Console Object for a specified Platform
     * @return a CompletableFuture<Console> with the Console object
     */
    override val console: CompletableFuture<Console>
        get() = CompletableFuture()

    /**
     * Gets a Console Object for a specified Platform
     * @return a CompletableFuture<Console> with the Console object
     */
    override fun getUser(uuid: UUID): CompletableFuture<User?> {
        TODO("Not yet implemented")
    }

    override fun getOfflineUser(uuid: UUID): CompletableFuture<OfflineUser?> {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        lateinit var instance: BukkitImpl
            private set
    }
}