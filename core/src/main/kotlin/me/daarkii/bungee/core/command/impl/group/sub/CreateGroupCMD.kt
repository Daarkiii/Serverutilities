package me.daarkii.bungee.core.command.impl.group.sub

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.utils.PlaceHolder

class CreateGroupCMD : SubCommand {

    override val names: MutableList<String> = mutableListOf("create", "creates")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        if(args.size != 2 && args.size != 3) {
            sender.sendMessage(config.getString("$messagePath.help"))
            return
        }

        val name = args[0]
        var potency = 0

        var permission = ""

        if(args.size == 3)
            permission = args[2]

        kotlin.runCatching {
            potency = args[1].toInt()
        }.onFailure {
            sender.sendMessage(config.getString("$messagePath.noNumber"))
            return
        }

        BungeeSystem.getInstance().groupHandler.getGroup(name).thenAccept { group ->

            if(group != null) {
                sender.sendMessage(config.getString("$messagePath.existAlready"), PlaceHolder("name", group.name))
                return@thenAccept
            }

            BungeeSystem.getInstance().groupHandler.createGroup(name, potency, permission, "&7").thenAccept { created ->
                sender.sendMessage(config.getString("$messagePath.groupCreated"), PlaceHolder("name", created.name))
            }
        }
    }

}