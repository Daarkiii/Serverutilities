package me.daarkii.bungee.core.utils

interface ConfigB {

    fun setUp()

    fun getString(path: String) : String

    fun getInteger(path: String) : Int

    fun getList(path: String) : List<*>

    fun getBoolean(path: String) : Boolean

}