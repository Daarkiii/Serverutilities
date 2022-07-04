package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.SubCommable
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.command.impl.group.sub.CreateSubCMD
import me.daarkii.bungee.core.config.impl.messages.Message

class GroupCMD(private val bungee: BungeeSystem) : SubCommable(
    bungee.commandFile.getString("group.name"),
    bungee.commandFile.getString("group.permission"),
    *bungee.commandFile.getStringList("group.aliases").toTypedArray()
) {

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    override val helpMsg: String = config.getString("$messagePath.help")

    override fun getSubCommands(): MutableList<SubCommand> {
        return mutableListOf(CreateSubCMD())
    }

}