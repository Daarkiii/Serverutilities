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

package me.daarkii.bungee.bungee.listener

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.event.impl.JoinEvent
import me.daarkii.bungee.core.event.impl.LeaveEvent
import me.daarkii.bungee.core.event.impl.LoginEvent
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerListener(private val bungeeSystem: BungeeSystem): Listener {

    @EventHandler
    fun handlePlayerJoin(event: ServerConnectEvent) {

        if(event.reason != ServerConnectEvent.Reason.JOIN_PROXY)
            return

        val user = bungeeSystem.getUser(event.player.uniqueId).join()!!
        val joinEvent = JoinEvent(user, "")

        //finally call event
        bungeeSystem.pluginHandler.callEvent(joinEvent)

        //send join message
        if(joinEvent.joinMessage != "")
            ProxyServer.getInstance().broadcast(TextComponent(joinEvent.joinMessage))
    }

    @EventHandler
    fun handlePlayerLogin(event: PostLoginEvent) {

        bungeeSystem.userHandler.createUser(event.player.uniqueId, event.player.name).join()

        bungeeSystem.getUser(event.player.uniqueId).thenAccept { user ->

            if(user == null)
                return@thenAccept

            val connectEvent = LoginEvent(user)

            //call event
            bungeeSystem.pluginHandler.callEvent(connectEvent)

            if(connectEvent.isCancelled) {
                user.kick(connectEvent.cancelReason)
            }

        }
    }

    @EventHandler
    fun handlePlayerLeave(event: PlayerDisconnectEvent) {

        bungeeSystem.getUser(event.player.uniqueId).thenAccept { user ->

            if(user == null)
                return@thenAccept

            //create event
            val leaveEvent = LeaveEvent(user, "")

            //call event
            bungeeSystem.pluginHandler.callEvent(leaveEvent)

            //send quit message
            if(leaveEvent.quitMessage != "")
                ProxyServer.getInstance().broadcast(TextComponent(leaveEvent.quitMessage))
        }

    }

    @EventHandler
    fun handlePlayerChat(event: ChatEvent) {

        if(event.sender !is ProxiedPlayer)
            return

        val player = event.sender as ProxiedPlayer

        bungeeSystem.getUser(player.uniqueId).thenAccept { user ->

            if(user == null)
                return@thenAccept

            //create event
            val chatEvent = if(event.isCommand || event.isProxyCommand)
                me.daarkii.bungee.core.event.impl.ChatEvent(event.isCancelled, user, event.message, true)
            else
                me.daarkii.bungee.core.event.impl.ChatEvent(event.isCancelled, user, event.message, false)

            //call event
            bungeeSystem.pluginHandler.callEvent(chatEvent)

            //implement methods
            event.isCancelled = chatEvent.isCancelled
        }

    }

}