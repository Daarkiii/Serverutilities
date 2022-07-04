package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.`object`.CommandSender

interface SubCommand {

    val names: MutableList<String>

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    fun execute(sender: CommandSender, args: List<String>)

}