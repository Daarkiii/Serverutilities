package me.daarkii.bungee.core.data

import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.TripleMap
import java.util.UUID

class UserRegistry {

    init {
        instance = this
    }

    private val users: TripleMap<UUID, Long, User> = TripleMap()
    private val offlineUsers: TripleMap<UUID, Long, OfflineUser> = TripleMap()

    /*
    Online user
     */

    fun getUser(uuid: UUID) : User? {
        return users.get(uuid)
    }

    fun getUser(id: Long) : User? {
        return users.get(id)
    }

    fun createUser(user: User) {
        this.users.insert(user.uuid, user.id, user)
    }

    /*
    Offline user
     */

    fun getOfflineUser(uuid: UUID) : OfflineUser? {
        return offlineUsers.get(uuid)
    }

    fun getOfflineUser(id: Long) : OfflineUser? {
        return offlineUsers.get(id)
    }

    fun createOfflineUser(user: OfflineUser) {
        this.offlineUsers.insert(user.uuid, user.id, user)
    }

    companion object {
        @JvmStatic
        lateinit var instance: UserRegistry
            private set
    }
}