package me.daarkii.bungee.core.handler

import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.event.Cancellable
import me.daarkii.bungee.core.event.Event
import me.daarkii.bungee.core.event.EventHandler
import java.util.EventListener
import java.util.concurrent.CompletableFuture

abstract class PluginHandler {

    /**
     * Safe EventListener class, so we can call events in these classes later
     */
    private val events: MutableSet<EventListener> = HashSet()

    /**
     * Registers a Command
     */
    abstract fun registerCommand(command: Command)

    /**
     * Registers an EventListener
     */
    fun registerEvent(eventListener: EventListener) {
        events.add(eventListener)
    }

    /**
     * Checks in every eventListener for a method which includes the event and run them
     */
    fun callEvent(event: Event) {

        for(eventListener in events) {

            for(method in eventListener.javaClass.methods) {

                if(!method.isAnnotationPresent(EventHandler::class.java))
                    continue

                if(method.parameterCount != 1)
                    continue

                val parameter = method.parameters[0]

                if(!Event::class.java.isAssignableFrom(parameter.type))
                    continue

                if(parameter.type.name != event.javaClass.name)
                    continue

                if(event !is Cancellable && event.isAsync) {
                    CompletableFuture.runAsync {
                        method.invoke(eventListener, event)
                        return@runAsync
                    }
                    continue
                }

                //run the event on the main thread
                kotlin.runCatching {
                    method.invoke(eventListener, event)
                }

            }
        }
    }
}