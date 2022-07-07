package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.`object`.CommandSender

interface SubCommand {

    /**
     * Here each name is added, with which the command can be executed
     */
    val names: MutableList<String>

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    fun execute(sender: CommandSender, args: Array<String>)

}