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

package me.daarkii.bungee.bukkit.impl.command

import me.daarkii.bungee.core.BungeeSystem
import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.config.impl.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.util.*

class BukkitCommand(private val command: Command) : BukkitCommand(command.name, "", "", command.aliases), TabCompleter {

    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {

        if(sender is Player) {

            BungeeSystem.getInstance().getUser(sender.uniqueId).thenAccept { user ->

                if(user != null) {
                    if(user.hasPermission(this.command.permission))
                        this.command.execute(user, args)
                    else
                        user.sendMessage(Message.instance.noPerms)
                }
            }
            return false
        }

        BungeeSystem.getInstance().console.thenAccept { console -> this.command.execute(console, args) }
        return false
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param alias The alias used
     * @param args The arguments passed to the command, including final
     * partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    override fun onTabComplete(sender: CommandSender?, command: org.bukkit.command.Command?, alias: String?, args: Array<out String>?): MutableList<String> {

        if(sender!! is Player) {

            val user = BungeeSystem.getInstance().getUser((sender as Player).uniqueId).join() ?: return Collections.emptyList()

            if(!user.hasPermission(this.command.permission))
                return Collections.emptyList()

            return this.command.handleTabCompletion(user, args!!).toMutableList()
        }

        return this.command.handleTabCompletion(BungeeSystem.getInstance().console.join(), args!!).toMutableList()
    }

}