package me.daarkii.bungee.core.event.impl

import me.daarkii.bungee.core.event.Event
import me.daarkii.bungee.core.`object`.User

/**
 * This should be used if an event returns the user object
 * This is already a subclass of the normal event so no normal event must be implemented
 */
abstract class GenericUserEvent : Event() {

    abstract val user: User

}