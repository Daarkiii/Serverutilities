/*
 * Copyright 2022 original authors & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.daarkii.bungee.core.handler

import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.event.*
import me.daarkii.bungee.core.event.EventListener
import me.daarkii.bungee.core.utils.TripleMap
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap
import kotlin.collections.HashSet

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

        val prioritySorted: MutableMap<EventPriority, MutableMap<Method, EventListener>> = ConcurrentHashMap()

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

                val priority = method.getAnnotation(EventHandler::class.java).priority

                val current: MutableMap<Method, EventListener> = if(prioritySorted.containsKey(priority))
                    prioritySorted[priority]!!
                else
                    HashMap()

                current[method] = eventListener
                prioritySorted[priority] = current
            }
        }

        val tripleMap: TripleMap<UUID, Long, Double> = TripleMap()

        Random().nextLong()

        //high priority
        if(prioritySorted.containsKey(EventPriority.HIGH)) {
            for(method in prioritySorted[EventPriority.HIGH]!!.keys) {

                if(event !is Cancellable && event.isAsync) {
                    CompletableFuture.runAsync {
                        method.invoke(prioritySorted[EventPriority.HIGH]!![method]!!, event)
                        return@runAsync
                    }
                    continue
                }

                //run the event on the main thread
                method.invoke(prioritySorted[EventPriority.HIGH]!![method]!!, event)
            }
        }

        //normal priority
        if(prioritySorted.containsKey(EventPriority.NORMAL)) {
            for(method in prioritySorted[EventPriority.NORMAL]!!.keys) {

                if(event !is Cancellable && event.isAsync) {
                    CompletableFuture.runAsync {
                        method.invoke(prioritySorted[EventPriority.NORMAL]!![method]!!, event)
                        return@runAsync
                    }
                    continue
                }

                //run the event on the main thread
                method.invoke(prioritySorted[EventPriority.NORMAL]!![method]!!, event)
            }
        }

        //low priority
        if(prioritySorted.containsKey(EventPriority.LOW)) {
            for(method in prioritySorted[EventPriority.LOW]!!.keys) {

                if(event !is Cancellable && event.isAsync) {
                    CompletableFuture.runAsync {
                        method.invoke(prioritySorted[EventPriority.LOW]!![method]!!, event)
                        return@runAsync
                    }
                    continue
                }

                //run the event on the main thread
                method.invoke(prioritySorted[EventPriority.LOW]!![method]!!, event)
            }
        }

    }
}