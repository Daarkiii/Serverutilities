package me.daarkii.bungee.bungee.listener

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.event.impl.JoinEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerListener(private val bungeeSystem: BungeeSystem): Listener {

    @EventHandler
    fun handlePlayerJoin(event: ServerConnectEvent) {

        if(event.reason != ServerConnectEvent.Reason.JOIN_PROXY)
            return

        bungeeSystem.userHandler.createUser(event.player.uniqueId, event.player.name).join()

        val user = bungeeSystem.getUser(event.player.uniqueId).join()!!
        val joinEvent = JoinEvent(user, "", event.isCancelled)

        //finally call event
        bungeeSystem.pluginHandler.callEvent(joinEvent)

        event.isCancelled = joinEvent.isCancelled
    }

}