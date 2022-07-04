package me.daarkii.bungee.core.utils

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier

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

    val size = this.wrappedOne.size

    fun clear() {
        this.wrappedOne.clear()
        this.wrappedTwo.clear()
        this.keyOne.clear()
        this.keyTwo.clear()
    }

    val values: MutableCollection<V> = this.keyOne.values

}