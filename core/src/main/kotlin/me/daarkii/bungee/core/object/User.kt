package me.daarkii.bungee.core.`object`

import net.kyori.adventure.text.Component

interface User : CommandSender, GenericUser {

    fun connect(server: String)

    fun kick(reason: String)

    fun sendTabList(header: Component, footer: Component)

    val address: String

    val serverName: String

    val ping: Int

    val highestGroup: Group

    val groups: List<Group>

}