package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.`object`.CommandSender
import java.util.*

abstract class SubCommendable(name: String, permission: String, vararg aliases: String) : Command(name, permission, *aliases) {

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

        var isFailure = true

        this.getSubCommands().forEach { subCommand ->
            subCommand.names.forEach { name ->
                if(name.equals(args[0], ignoreCase = true)) {
                    subCommand.execute(sender, remainingArgs.toTypedArray())
                    isFailure = false
                }
            }
        }

        if(isFailure)
            this.onFailure(sender, args)

    }

    abstract val helpMsg: String

    /**
     * Gets executed when no Subcommand was executed
     * Default it will send the help message to the sender
     */
    open fun onFailure(sender: CommandSender, args: Array<out String>) {
        sender.sendMessage(helpMsg)
    }

    abstract override fun getSubCommands(): MutableList<SubCommand>
}