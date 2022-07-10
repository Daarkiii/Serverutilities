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

package me.daarkii.bungee.core.command

import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.utils.PlaceHolder
import java.util.*

abstract class SubCommendable(name: String, permission: String, vararg aliases: String) : Command(name, permission, *aliases) {

    override fun execute(sender: CommandSender, args: Array<out String>) {

        if(args.isEmpty()) {
            sender.sendMessage(helpMsg, PlaceHolder("prefix", Message.instance.prefix))
            return
        }

        val remainingArgs: MutableList<String> = LinkedList()

        for(arg in args) {
            if(arg != args[0])
                remainingArgs.add(arg)
        }

        var isFailure = true

        this.getSubCommands().forEach { subCommand ->
            subCommand.names.forEach { name ->
                if(name.equals(args[0], ignoreCase = true)) {
                    subCommand.execute(sender, remainingArgs.toTypedArray())
                    isFailure = false
                }
            }
        }

        if(isFailure)
            this.onFailure(sender, args)

    }

    abstract val helpMsg: String

    /**
     * Gets executed when no Subcommand was executed
     * Default it will send the help message to the sender
     */
    open fun onFailure(sender: CommandSender, args: Array<out String>) {
        sender.sendMessage(helpMsg, PlaceHolder("prefix", Message.instance.prefix))
    }

    abstract override fun getSubCommands(): MutableList<SubCommand>
}