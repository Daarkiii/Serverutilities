package me.daarkii.bungee.bungee.impl

import me.daarkii.bungee.core.BungeeSystem
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

class BungeeCommand(private val command: me.daarkii.bungee.core.command.Command) : Command(command.getName(), command.getPermission(), *command.getAliases().toTypedArray()) {

    /**
     * Execute this command with the specified sender and arguments.
     *
     * @param sender the executor of this command
     * @param args arguments used to invoke this command
     */
    override fun execute(sender: CommandSender?, args: Array<out String>?) {

        if(sender!! is ProxiedPlayer) {
            BungeeSystem.getInstance().getUser((sender as ProxiedPlayer).uniqueId).thenAccept { user -> command.execute(user, args!!) }
            return
        }

        BungeeSystem.getInstance().getConsole().thenAccept { console -> command.execute(console, args!!) }
    }


}