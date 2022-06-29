package me.daarkii.bungee.core.`object`

interface User : CommandSender, GenericUser {

    fun connect(server: String)

    fun kick(reason: String)

    fun getAddress() : String

    fun getServerName() : String

    fun getPing() : Int

}