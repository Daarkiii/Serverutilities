package me.daarkii.bungee.core.event.impl

import me.daarkii.bungee.core.event.Cancellable
import me.daarkii.bungee.core.`object`.User

class JoinEvent(
    override val user: User,
    var joinMessage: String,
    override var isCancelled: Boolean
) : GenericUserEvent(), Cancellable {

    override val isAsync = false

}