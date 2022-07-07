package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.utils.PlaceHolder

class CreateCMD : SubCommand {

    override val names: MutableList<String> = mutableListOf("create", "creates")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        //<group> <create> || <name> <potency> <permission>
        if(args.size != 3) {
            sender.sendMessage(config.getString("$messagePath.help"))
            return
        }

        val name = args[0].lowercase()
        val permission = args[2]

        var potency = 0

        kotlin.runCatching {
            potency = args[1].toInt()
        }.onFailure {
            sender.sendMessage(config.getString("$messagePath.noNumber"))
            return
        }

        if(potency < 1 || potency > 100) {
            sender.sendMessage(config.getString("$messagePath.falsePotency"))
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