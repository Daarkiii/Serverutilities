package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.`object`.Group
import java.util.LinkedList

class SetCMD(private val group: Group) : SubCommand {

    override val names: MutableList<String> = mutableListOf("set", "change", "sets", "changes")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        //<group> <groupName> || <set> <color, potency..> <value>
        if(args.size != 3) {
            sender.sendMessage(config.getString("$messagePath.help"))
            return
        }

        val remainingArgs: MutableList<String> = LinkedList()

        //build new args without args[0]
        for(line in args) {
            remainingArgs.add(line)
        }

        //remove the first and the seconds line, so we get the desired format
        remainingArgs.removeAt(0)
        remainingArgs.removeAt(0)

        val subCommands: List<SubCommand> = listOf(
            SetColorCMD(group),
            SetPotencyCMD(group),
            SetNameCMD(group),
            SetPermissionCMD(group),
            SetDefaultCMD(group))

        for(subCommand in subCommands) {
            for(name in subCommand.names) {
                if(args[1] == name) {
                    subCommand.execute(sender, remainingArgs.toTypedArray())
                    return
                }
            }
        }

        //There is no subcommand which equals the given name
        sender.sendMessage(config.getString("$messagePath.help"))
    }

}