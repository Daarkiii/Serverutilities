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
import me.daarkii.bungee.core.command.SubCommendable
import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.utils.PlaceHolder
import java.util.LinkedList

class GroupCMD(private val bungee: BungeeSystem) : SubCommendable(
    bungee.commandFile.getString("group.name"),
    bungee.commandFile.getString("group.permission"),
    *bungee.commandFile.getStringList("group.aliases").toTypedArray()
) {

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    override val helpMsg: String = config.getString("$messagePath.help")

    override fun getSubCommands(): MutableList<SubCommand> {
        return mutableListOf(CreateGroupCMD(), DeleteGroupCMD())
    }

    override fun onFailure(sender: CommandSender, args: Array<out String>) {

        bungee.groupHandler.getGroup(args[0]).thenAccept { group ->

            if(group == null) {
                sender.sendMessage(config.getString("$messagePath.notExist"), PlaceHolder("name", args[0]), PlaceHolder("prefix", Message.instance.prefix))
                return@thenAccept
            }

            val subCommands = listOf(GroupSetCMD(group), GroupInfoCMD(group))

            val finalArgs: MutableList<String> = LinkedList()
            var isExecuted = false

            for (line in args) {
                finalArgs.add(line)
            }

            //remove the first  line, so we get the desired format
            finalArgs.removeAt(0)

            if(args.size > 1) {

                for(subCommand in subCommands) {
                    for(name in subCommand.names) {
                        if(args[1].equals(name, ignoreCase = true)) {
                            subCommand.execute(sender, finalArgs.toTypedArray())
                            isExecuted = true
                        }
                    }
                }

                if(isExecuted)
                    return@thenAccept
            }

            GroupInfoCMD(group).execute(sender, finalArgs.toTypedArray())
        }
    }

}