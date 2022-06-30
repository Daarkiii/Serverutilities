package me.daarkii.bungee.core.`object`

import java.util.UUID

interface Server {

    val proxyPlayers: Int

    val proxySlots: Int

    fun getName(uuid: UUID) : String

    fun getPlayers(uuid: UUID) : Int

}