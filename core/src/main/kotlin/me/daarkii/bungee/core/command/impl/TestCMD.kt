package me.daarkii.bungee.core.command.impl

import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender

class TestCMD : Command("test", "", "") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        sender.sendMessage(Message.instance.noPerms)
    }

}