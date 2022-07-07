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

class SetGroupPotencyCMD(private val group: Group) : SubCommand {

    override val names: MutableList<String> = mutableListOf("potency")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        var potency = 0

        kotlin.runCatching {
            potency = args[0].toInt()
        }.onFailure {
            sender.sendMessage(config.getString("$messagePath.noNumber"))
            return
        }

        if(potency < 1 || potency > 100) {
            sender.sendMessage(config.getString("$messagePath.falsePotency"))
            return
        }

        group.potency = potency
        sender.sendMessage(config.getString("$messagePath.changedPotency"),
            PlaceHolder("group", Message.Wrapper.wrap(group.color + group.name + "</c>")),
            PlaceHolder("value", Message.Wrapper.wrap(potency.toString()))
        )
    }

}