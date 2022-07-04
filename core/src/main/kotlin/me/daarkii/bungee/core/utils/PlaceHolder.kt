package me.daarkii.bungee.core.utils

import me.daarkii.bungee.core.config.impl.messages.Message
import net.kyori.adventure.text.Component

class PlaceHolder {

    var name: String = ""
    var component: Component = Component.text("")

    constructor(name: String, component: Component) {
        this.name = name
        this.component = component
    }

    constructor(name: String, value: String) {
        this.name = name
        this.component = Message.Wrapper.wrap(value)
    }

}