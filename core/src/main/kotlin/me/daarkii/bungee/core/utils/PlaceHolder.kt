package me.daarkii.bungee.core.utils

import net.kyori.adventure.text.Component

data class PlaceHolder(private val nameImpl: String, private val componentImpl: Component) {

    val name: String
        get() = nameImpl

    val component: Component
        get() = componentImpl
}