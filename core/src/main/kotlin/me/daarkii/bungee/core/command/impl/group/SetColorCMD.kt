package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.`object`.Group
import me.daarkii.bungee.core.utils.PlaceHolder

class SetColorCMD(private val group: Group) : SubCommand {

    override val names: MutableList<String> = mutableListOf("color")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        val replaced = args[0]
            .replace("&", "")
            .replace("§", "")

        if(replaced.length != 1) {
            sender.sendMessage(config.getString("$messagePath.falseColor"))
            return
        }

        if(!Message.Wrapper.migrate(replaced).contains("<c")) {
            sender.sendMessage(config.getString("$messagePath.falseColor"))
            return
        }

        group.color = "&$replaced"
        sender.sendMessage(config.getString("$messagePath.changedColor"),
            PlaceHolder("group", Message.Wrapper.wrap(group.color + group.name + "</c>")),
            PlaceHolder("value", Message.Wrapper.wrap(replaced))
        )
    }

}