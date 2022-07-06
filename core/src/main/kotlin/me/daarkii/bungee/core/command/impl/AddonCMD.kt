/*
 * Copyright 2022 original authors & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.daarkii.bungee.core.command.impl

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.utils.PlaceHolder
import net.kyori.adventure.text.Component

class AddonCMD(private val bungee: BungeeSystem) : Command(
    bungee.commandFile.getString("addons.name"),
    bungee.commandFile.getString("addons.permission"),
    *bungee.commandFile.getStringList("addons.aliases").toTypedArray()
) {

    override fun execute(sender: CommandSender, args: Array<out String>) {

        val builder = StringBuilder()
        val messageFile = Message.instance.config

        for(addon in bungee.addonHandler.addons) {
            if(builder.toString() == "")
                builder.append(messageFile.getString("messages.commands.addons.addonColor") + addon.addonInfo.name + "</c>")
            else
                builder.append(messageFile.getString("messages.commands.addons.commaColor") + ",</c> " + messageFile.getString("messages.commands.addons.addonColor") + addon.addonInfo.name + "</c>")
        }

        sender.sendMessage(Message.Wrapper.wrap(messageFile.getString("messages.commands.addons.message"),
            PlaceHolder("prefix", Message.instance.prefix),
            PlaceHolder("size", Component.text("${bungee.addonHandler.addons.size}")),
            PlaceHolder("addons", Message.Wrapper.wrap("$builder")))
        )
    }

}