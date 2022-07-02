package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.core.`object`.OfflineUser
import net.md_5.bungee.api.ProxyServer
import java.util.*

class BungeeOfflineUser(
    override val id: Long,
    override val uuid: UUID,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long
) : OfflineUser {

    override val isOnline = ProxyServer.getInstance().getPlayer(uuid) != null

}