package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.SubCommendable
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.command.impl.group.sub.CreateGroupCMD
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender

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
        sender.executeCommand(bungee.commandFile.getString("group.name") + " info " + args[0])
    }

}