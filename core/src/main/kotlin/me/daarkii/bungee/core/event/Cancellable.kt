package me.daarkii.bungee.core.event

/**
 * Generally used in Events to make clear that an event can be cancelled
 * Events with include this can't run async
 */
interface Cancellable {

    var isCancelled: Boolean

}