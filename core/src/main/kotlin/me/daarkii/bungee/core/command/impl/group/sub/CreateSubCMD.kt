package me.daarkii.bungee.core.command.impl.group.sub

import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.`object`.CommandSender

class CreateSubCMD() : SubCommand {

    override val names: MutableList<String> = mutableListOf("create", "creates")

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<out String>) {
        TODO("Not yet implemented")
    }

}