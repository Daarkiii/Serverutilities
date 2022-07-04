package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.core.`object`.OfflineUser
import net.md_5.bungee.api.ProxyServer
import java.util.*

abstract class BungeeOfflineUser(
    override val id: Long,
    override val uuid: UUID,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long,
    override val name: String
) : OfflineUser {

    override val isOnline = ProxyServer.getInstance().getPlayer(uuid) != null

    abstract override val displayName: String

    override fun equals(other: Any?): Boolean {

        if(other == null)
            return false

        if(this === other)
            return true

        if(other !is OfflineUser)
            return false

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }

}