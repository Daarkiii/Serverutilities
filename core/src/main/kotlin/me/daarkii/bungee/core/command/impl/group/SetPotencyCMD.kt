package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.`object`.Group
import me.daarkii.bungee.core.utils.PlaceHolder

class SetPotencyCMD(private val group: Group) : SubCommand {

    override val names: MutableList<String> = mutableListOf("potency")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        var potency = 0

        kotlin.runCatching {
            potency = args[0].toInt()
        }.onFailure {
            sender.sendMessage(config.getString("$messagePath.noNumber"))
            return
        }

        group.potency = potency
        sender.sendMessage(config.getString("$messagePath.changedPotency"), PlaceHolder("group", Message.Wrapper.wrap(group.color + group.name + "</c>")))
    }

}