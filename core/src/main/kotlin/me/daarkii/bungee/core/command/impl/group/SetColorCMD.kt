package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.`object`.Group

class SetColorCMD(private val group: Group) : SubCommand {

    override val names: MutableList<String> = mutableListOf("color")

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

    }

}