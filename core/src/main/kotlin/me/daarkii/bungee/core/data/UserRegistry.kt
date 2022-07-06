package me.daarkii.bungee.core.data

import me.daarkii.bungee.core.`object`.OfflineUser
import me.daarkii.bungee.core.`object`.User
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class UserRegistry {

    init {
        instance = this
    }

    private val users: MutableMap<UUID, User> = ConcurrentHashMap()
    private val offlineUsers: MutableMap<UUID, OfflineUser> = ConcurrentHashMap()

    /*
    Online user
     */

    fun getUser(uuid: UUID) : User? {
        return users[uuid]
    }

    fun createUser(user: User) {
        this.users[user.uuid] =  user
    }

    /*
    Offline user
     */

    fun getOfflineUser(uuid: UUID) : OfflineUser? {
        return offlineUsers[uuid]
    }

    fun createOfflineUser(user: OfflineUser) {
        this.offlineUsers[user.uuid] =  user
    }

    companion object {
        @JvmStatic
        lateinit var instance: UserRegistry
            private set
    }
}