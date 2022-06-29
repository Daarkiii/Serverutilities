package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.`object`.CommandSender
import java.util.*

abstract class Command(
    private val name: String,
    private val permission: String,
    private vararg val aliases: String
) {

    abstract fun execute(sender: CommandSender, args: Array<out String>)

    open fun handleTabCompletion(sender: CommandSender, args: Array<String>) : Iterable<String> {
        return Collections.emptyList()
    }

    fun getName() : String {
        return name
    }

    fun getPermission() : String {
        return permission
    }

    fun getAliases() : MutableList<String> {
        return mutableListOf(*aliases)
    }
}