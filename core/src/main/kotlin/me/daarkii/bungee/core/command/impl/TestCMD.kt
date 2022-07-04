package me.daarkii.bungee.core.command.impl

import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.utils.PlaceHolder
import net.kyori.adventure.text.Component

class TestCMD : Command("test", "", "") {

    override fun execute(sender: CommandSender, args: Array<out String>) {

        val msg = Message.Wrapper.wrap("&7Hello &l<player> &bwelcome", PlaceHolder("player", Component.text(sender.name)))

        sender.sendMessage(msg)
    }

}