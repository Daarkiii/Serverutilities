package me.daarkii.bungee.core.`object`

interface User : CommandSender, GenericUser {

    fun connect(server: String)

    fun kick(reason: String)

    val address: String

    val serverName: String

    val ping: Int

}