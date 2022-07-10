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
import me.daarkii.bungee.core.utils.PlaceHolder

class CreateGroupCMD : SubCommand {

    override val names: MutableList<String> = mutableListOf("create", "creates")

    private val config = Message.instance.config
    private val messagePath = "messages.commands.group"

    /**
     * Executes a subcommand
     * @param sender the Commander who sent the main commands
     * @param args The arguments of the main command, without args[0]
     */
    override fun execute(sender: CommandSender, args: Array<String>) {

        //<group> <create> || <name> <potency> <permission>
        if(args.size != 3) {
            sender.sendMessage(config.getString("$messagePath.help"), PlaceHolder("prefix", Message.instance.prefix))
            return
        }

        val name = args[0].lowercase()
        val permission = args[2]

        var potency = 0

        kotlin.runCatching {
            potency = args[1].toInt()
        }.onFailure {
            sender.sendMessage(config.getString("$messagePath.noNumber"), PlaceHolder("prefix", Message.instance.prefix))
            return
        }

        if(potency < 1 || potency > 100) {
            sender.sendMessage(config.getString("$messagePath.falsePotency"), PlaceHolder("prefix", Message.instance.prefix))
            return
        }

        BungeeSystem.getInstance().groupHandler.getGroup(name).thenAccept { group ->

            if(group != null) {
                sender.sendMessage(config.getString("$messagePath.existAlready"), PlaceHolder("name", group.name), PlaceHolder("prefix", Message.instance.prefix))
                return@thenAccept
            }

            BungeeSystem.getInstance().groupHandler.createGroup(name, potency, permission, "&7").thenAccept { created ->
                sender.sendMessage(config.getString("$messagePath.groupCreated"), PlaceHolder("name", created.name), PlaceHolder("prefix", Message.instance.prefix))
            }

        }
    }

}