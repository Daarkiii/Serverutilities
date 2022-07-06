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

package me.daarkii.bungee.core.utils

import java.util.concurrent.ConcurrentHashMap

class TripleMap<K, S, V> {

    private val keyOne: MutableMap<K, V> = ConcurrentHashMap()
    private val keyTwo: MutableMap<S, V> = ConcurrentHashMap()

    private val wrappedOne: MutableMap<K, S> = ConcurrentHashMap()
    private val wrappedTwo: MutableMap<S, K> = ConcurrentHashMap()

    fun insert(keyOne: K, keyTwo: S, value: V) {

        //Value holder
        this.keyOne[keyOne] = value
        this.keyTwo[keyTwo] = value

        //Key holder
        this.wrappedOne[keyOne] = keyTwo
        this.wrappedTwo[keyTwo] = keyOne
    }

    @JvmName("getValueOne")
    fun get(keyOne: K) : V? {
        return this.keyOne[keyOne]
    }

    @JvmName("getValueTwo")
    fun get(keyTwo: S) : V? {
        return this.keyTwo[keyTwo]
    }

    @JvmName("getKeyTwo")
    fun getKey(keyOne: K) : S? {
        return this.wrappedOne[keyOne]
    }

    @JvmName("getKeyOne")
    fun getKey(keyTwo: S) : K? {
        return this.wrappedTwo[keyTwo]
    }

    @JvmName("removeWithKeyOne")
    fun remove(key: K) {
        val secondKey = this.getKey(key)

        if(secondKey != null) {
            this.wrappedTwo.remove(secondKey)
            this.keyTwo.remove(secondKey)
        }

        this.wrappedOne.remove(key)
        this.keyOne.remove(key)
    }

    @JvmName("removeWithKeyTwo")
    fun remove(secondKey: S) {
        val key = this.getKey(secondKey)

        if(key != null) {
            this.wrappedOne.remove(key)
            this.keyOne.remove(key)
        }

        this.wrappedTwo.remove(secondKey)
        this.keyTwo.remove(secondKey)
    }

    fun clear() {
        this.wrappedOne.clear()
        this.wrappedTwo.clear()
        this.keyOne.clear()
        this.keyTwo.clear()
    }

}