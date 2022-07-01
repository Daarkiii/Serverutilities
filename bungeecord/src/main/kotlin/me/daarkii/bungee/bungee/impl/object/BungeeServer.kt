package me.daarkii.bungee.bungee.impl.`object`

import me.daarkii.bungee.core.`object`.Server
import net.md_5.bungee.api.plugin.Plugin
import java.util.*

class BungeeServer(private val plugin: Plugin) : Server {

    override val proxyPlayers = plugin.proxy.onlineCount

    override val proxySlots = plugin.proxy.config.playerLimit

    override fun getName(uuid: UUID) : String {
        return plugin.proxy.getPlayer(uuid).server.info.name
    }

    override fun getPlayers(uuid: UUID): Int {
        return plugin.proxy.getPlayer(uuid).server.info.players.size
    }

}