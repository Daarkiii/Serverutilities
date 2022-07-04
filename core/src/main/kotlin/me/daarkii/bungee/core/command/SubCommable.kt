package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.`object`.CommandSender
import java.util.*

abstract class SubCommable(name: String, permission: String, vararg aliases: String) : Command(name, permission, *aliases) {

    override fun execute(sender: CommandSender, args: Array<out String>) {

        if(args.isEmpty()) {
            sender.sendMessage(helpMsg)
            return
        }

        val remainingArgs: MutableList<String> = LinkedList()

        for(arg in args) {
            if(arg != args[0])
                remainingArgs.add(arg)
        }

        this.getSubCommands().forEach { subCommand ->
            subCommand.names.forEach { name ->
                if(name.equals(args[0], ignoreCase = true))
                    subCommand.execute(sender, remainingArgs)
            }
        }

    }

    abstract val helpMsg: String

    abstract override fun getSubCommands(): MutableList<SubCommand>
}