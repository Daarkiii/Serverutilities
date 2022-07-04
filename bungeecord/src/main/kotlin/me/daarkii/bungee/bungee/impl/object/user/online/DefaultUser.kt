package me.daarkii.bungee.bungee.impl.`object`.user.online

import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeUser
import net.md_5.bungee.api.ProxyServer
import java.util.*

class DefaultUser(
    id: Long,
    uuid: UUID,
    proxy: BungeeImpl,
    firstJoin: Long,
    lastJoin: Long,
    onlineTime: Long
) : BungeeUser(id, uuid, proxy, firstJoin, lastJoin, onlineTime) {

    override val displayName: String = ProxyServer.getInstance().getPlayer(uuid).displayName

    override fun connect(server: String) {

        val player = ProxyServer.getInstance().getPlayer(uuid)
        val serverInfo = ProxyServer.getInstance().getServerInfo(server)

        if(player != null && serverInfo != null)
            player.connect(serverInfo)
    }
}