package me.daarkii.bungee.bungee.impl.`object`.user.online

import de.dytanic.cloudnet.driver.CloudNetDriver
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager
import me.daarkii.bungee.bungee.impl.BungeeImpl
import me.daarkii.bungee.bungee.impl.`object`.user.BungeeUser
import java.util.*

class CloudNetUser(
    id: Long,
    uuid: UUID,
    proxy: BungeeImpl,
    firstJoin: Long,
    lastJoin: Long,
    onlineTime: Long,
) : BungeeUser(id, uuid, proxy, firstJoin, lastJoin, onlineTime) {

    private val playerManager = CloudNetDriver.getInstance().servicesRegistry.getFirstService(IPlayerManager::class.java)

    override val displayName: String
        get() {
            val user =  CloudNetDriver.getInstance().permissionManagement.getUser(uuid) ?: return name
            return CloudNetDriver.getInstance().permissionManagement.getHighestPermissionGroup(user).color.replace("&", "§") + name
        }

    override fun connect(server: String) {
        playerManager.getPlayerExecutor(uuid).connect(server)
    }

}