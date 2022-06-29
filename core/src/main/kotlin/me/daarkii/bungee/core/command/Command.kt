package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.`object`.CommandSender
import java.util.*

abstract class Command(
    private val nameObj: String,
    private val permissionObj: String,
    private vararg val aliasesObj: String
) {

    abstract fun execute(sender: CommandSender, args: Array<out String>)

    open fun handleTabCompletion(sender: CommandSender, args: Array<out String>) : MutableIterable<String> {
        return Collections.emptyList()
    }

    val name: String
        get() = nameObj

    val permission: String
        get() = permissionObj

    val aliases: MutableList<String>
        get() = mutableListOf(*aliasesObj)

}