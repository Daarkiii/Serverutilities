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