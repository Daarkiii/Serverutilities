package me.daarkii.bungee.bungee.impl.`object`.user.offline

import de.dytanic.cloudnet.driver.CloudNetDriver
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeOfflineUser
import java.util.*

class CloudNetOfflineUser(
    override val id: Long,
    override val uuid: UUID,
    override val firstJoin: Long,
    override val lastJoin: Long,
    override var onlineTime: Long,
    override val name: String
) : BungeeOfflineUser(id, uuid, firstJoin, lastJoin, onlineTime, name) {

    override val displayName: String
        get() {
            val permissionUser = CloudNetDriver.getInstance().permissionManagement.getUser(uuid) ?: return name
            return permissionUser.name
        }

}