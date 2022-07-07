package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.SubCommendable
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import java.util.LinkedList

class GroupCMD(private val bungee: BungeeSystem) : SubCommendable(
    bungee.commandFile.getString("group.name"),
    bungee.commandFile.getString("group.permission"),
    *bungee.commandFile.getStringList("group.aliases").toTypedArray()
) {

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    override val helpMsg: String = config.getString("$messagePath.help")

    override fun getSubCommands(): MutableList<SubCommand> {
        return mutableListOf(CreateGroupCMD())
    }

    override fun onFailure(sender: CommandSender, args: Array<out String>) {

        bungee.groupHandler.getGroup(args[0]).thenAccept { group ->

            if(group == null) {
                sender.sendMessage(config.getString("$messagePath.notExist"))
                return@thenAccept
            }

            val subCommands = listOf(SetCMD(group), InfoCMD(group))

            val finalArgs: MutableList<String> = LinkedList()
            var isExecuted = false

            for (line in args) {
                if(line != args[0])
                    finalArgs.add(line)
            }

            if(args.size > 1) {

                for(subCommand in subCommands) {
                    for(name in subCommand.names) {
                        if(args[1].equals(name, ignoreCase = true)) {
                            subCommand.execute(sender, finalArgs.toTypedArray())
                            isExecuted = true
                        }
                    }
                }

                if(isExecuted)
                    return@thenAccept
            }

            InfoCMD(group).execute(sender, finalArgs.toTypedArray())
        }
    }

}