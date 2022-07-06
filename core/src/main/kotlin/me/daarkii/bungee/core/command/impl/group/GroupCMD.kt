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

            println(args[0])
            println(args[0])

            println(group)
            println(group)

            if(group == null) {
                sender.sendMessage(config.getString("$messagePath.notExist"))
                return@thenAccept
            }

            val subCommand = SetCMD(group)

            if(args.size > 1) {

                val finalArgs: MutableList<String> = LinkedList()

                for (line in args) {
                    if(line != args[0])
                        finalArgs.add(line)
                }

                for(name in subCommand.names) {
                    if(args[1] == name) {
                        subCommand.execute(sender, finalArgs.toTypedArray())
                        return@thenAccept
                    }
                }
            }

            sender.executeCommand(bungee.commandFile.getString("group.name") + " info " + args[0])
        }
    }

}