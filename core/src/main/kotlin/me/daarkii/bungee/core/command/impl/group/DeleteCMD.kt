package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.utils.PlaceHolder

class DeleteCMD : SubCommand {

    override val names: MutableList<String> = mutableListOf("delete", "remove")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        if(args.size != 1) {
            sender.sendMessage(config.getString("$messagePath.help"))
            return
        }

        BungeeSystem.getInstance().groupHandler.getGroup(args[0]).thenAccept { group ->

            if(group == null) {
                sender.sendMessage(config.getString("$messagePath.notExist"), PlaceHolder("name", args[0]))
                return@thenAccept
            }

            BungeeSystem.getInstance().groupHandler.deleteGroup(group)
            sender.sendMessage(config.getString("$messagePath.deletedGroup"), PlaceHolder("group", Message.Wrapper.wrap(group.color + group.name + "</c>")))
        }

    }

}