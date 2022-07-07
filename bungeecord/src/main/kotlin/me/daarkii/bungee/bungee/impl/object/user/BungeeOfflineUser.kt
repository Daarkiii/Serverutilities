package me.daarkii.bungee.bungee.impl.`object`.user

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.`object`.Group
import me.daarkii.bungee.core.`object`.OfflineUser
import net.md_5.bungee.api.ProxyServer
import java.util.*

abstract class BungeeOfflineUser(
    override val id: Long,
    override val uuid: UUID,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long,
    override val name: String,
    private val groupID: Int
) : OfflineUser {

    override val isOnline
        get() = ProxyServer.getInstance().getPlayer(uuid) != null

    abstract override val displayName: String

    override val lastKnownGroup: Group?
        get() {
            return BungeeSystem.getInstance().groupHandler.getGroup(groupID).join()
        }

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