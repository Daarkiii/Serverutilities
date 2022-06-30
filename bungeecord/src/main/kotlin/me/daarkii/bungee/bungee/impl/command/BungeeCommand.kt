package me.daarkii.bungee.bungee.impl.command

import me.daarkii.bungee.core.BungeeSystem
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import java.util.Collections

class BungeeCommand(private val command: me.daarkii.bungee.core.command.Command) : Command(command.name, "", *command.aliases.toTypedArray()), TabExecutor {

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param args arguments used to invoke this command
     */
    override fun execute(sender: CommandSender?, args: Array<out String>?) {

        if(sender!! is ProxiedPlayer) {
            BungeeSystem.getInstance().getUser((sender as ProxiedPlayer).uniqueId).thenAccept { user ->
                if(user != null) {
                    if(user.hasPermission(command.permission))
                        command.execute(user, args!!)
                    else
                        user.sendMessage("No Perms")
                }
            }
            return
        }

        BungeeSystem.getInstance().console.thenAccept { console -> command.execute(console, args!!) }
    }

    override fun onTabComplete(sender: CommandSender?, args: Array<out String>?): MutableIterable<String> {

        if(sender!! is ProxiedPlayer) {

            val user = BungeeSystem.getInstance().getUser((sender as ProxiedPlayer).uniqueId).join() ?: return Collections.emptyList()

            if(!user.hasPermission(command.permission))
                return Collections.emptyList()

            return command.handleTabCompletion(user, args!!)
        }

        return command.handleTabCompletion(BungeeSystem.getInstance().console.join(), args!!)
    }


}