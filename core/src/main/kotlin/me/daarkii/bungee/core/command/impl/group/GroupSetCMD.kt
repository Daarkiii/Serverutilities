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

import me.daarkii.bungee.core.command.SubCommand
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.`object`.Group
import me.daarkii.bungee.core.utils.PlaceHolder
import java.util.LinkedList

class GroupSetCMD(private val group: Group) : SubCommand {

    override val names: MutableList<String> = mutableListOf("set", "change", "sets", "changes")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        //<group> <groupName> || <set> <color, potency..> <value>
        if(args.size != 3) {
            sender.sendMessage(config.getString("$messagePath.help"), PlaceHolder("prefix", Message.instance.prefix))
            return
        }

        val remainingArgs: MutableList<String> = LinkedList()

        //build new args without args[0]
        for(line in args) {
            remainingArgs.add(line)
        }

        //remove the first and the seconds line, so we get the desired format
        remainingArgs.removeAt(0)
        remainingArgs.removeAt(0)

        val subCommands: List<SubCommand> = listOf(
            SetColorCMD(group),
            SetGroupPotencyCMD(group),
            SetGroupNameCMD(group),
            SetGroupPermissionCMD(group),
            SetDefaultGroupCMD(group))

        for(subCommand in subCommands) {
            for(name in subCommand.names) {
                if(args[1] == name) {
                    subCommand.execute(sender, remainingArgs.toTypedArray())
                    return
                }
            }
        }

        //There is no subcommand which equals the given name
        sender.sendMessage(config.getString("$messagePath.help"), PlaceHolder("prefix", Message.instance.prefix))
    }

}