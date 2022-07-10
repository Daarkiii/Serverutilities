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

package me.daarkii.bungee.core.command.impl.group

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.`object`.Group
import me.daarkii.bungee.core.utils.PlaceHolder

class SetGroupNameCMD(private val group: Group) : SubCommand {

    override val names: MutableList<String> = mutableListOf("name", "alias", "names")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        val name = args[0]

        BungeeSystem.getInstance().groupHandler.getGroup(name).thenAccept {

            if(it != null) {
                sender.sendMessage(config.getString("$messagePath.existAlready"),
                    PlaceHolder("name", Message.Wrapper.wrap(it.color + it.name + "</c>")),
                    PlaceHolder("prefix", Message.instance.prefix))
                return@thenAccept
            }

            //Update in Cache
            BungeeSystem.getInstance().groupHandler.changeGroupName(group.name, group)

            //Update the object and send message to the User
            group.name = name

            sender.sendMessage(config.getString("$messagePath.changedName"),
                PlaceHolder("prefix", Message.instance.prefix),
                PlaceHolder("group", Message.Wrapper.wrap(group.color + group.name + "</c>")),
                PlaceHolder("value", Message.Wrapper.wrap(name)))
        }
    }

}