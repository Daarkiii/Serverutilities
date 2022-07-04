package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.`object`.CommandSender
import java.util.*

abstract class Command(
    val name: String,
    val permission: String,
    private vararg val aliasesObj: String
) {

    abstract fun execute(sender: CommandSender, args: Array<out String>)

    open fun handleTabCompletion(sender: CommandSender, args: Array<out String>) : MutableIterable<String> {
        return Collections.emptyList()
    }

    open fun getSubCommands() : MutableList<SubCommand> {
        return Collections.emptyList()
    }

    val aliases: MutableList<String>
        get() = mutableListOf(*aliasesObj)

}