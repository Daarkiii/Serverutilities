package me.daarkii.bungee.core.event

/**
 * Makes clear that a class is an Event which can be fired
 */
abstract class Event {

    abstract val isAsync: Boolean

}