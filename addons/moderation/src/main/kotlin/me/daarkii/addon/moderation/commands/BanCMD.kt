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

package me.daarkii.addon.moderation.commands

import me.daarkii.addon.moderation.Moderation
import me.daarkii.addon.moderation.`object`.HistoryEntry
import me.daarkii.addon.moderation.`object`.Type
import me.daarkii.bungee.core.command.Command
import me.daarkii.bungee.core.config.impl.messages.Message
import me.daarkii.bungee.core.`object`.CommandSender
import me.daarkii.bungee.core.`object`.User
import me.daarkii.bungee.core.utils.PlaceHolder

class BanCMD(private val moderation: Moderation) : Command(
    moderation.system.commandFile.getString("ban.name"),
    moderation.system.commandFile.getString("ban.permission"),
    *moderation.system.commandFile.getStringList("ban.aliases").toTypedArray()
) {

    private val config = moderation.messages
    private val path = "messages.punish"

    private val historyHandler = this.moderation.historyHandler
    private val banHandler = this.moderation.banHandler

    override fun execute(sender: CommandSender, args: Array<out String>) {

        //<user> <reason_id>/<reason_name>?--override

        var shouldOverride = false

        if(args.size != 2 && args.size != 3) {
            sender.sendMessage(config.getString("$path.help"), PlaceHolder("prefix", config.getString("messages.prefix")))
            return
        }

        if(args.size == 3) {
            if(args[2] != "--override") {
                sender.sendMessage(config.getString("$path.help"), PlaceHolder("prefix", config.getString("messages.prefix")))
                return
            }
            if(!sender.hasPermission(moderation.system.commandFile.getString("ban.overridepermission"))) {
                sender.sendMessage(Message.instance.noPerms)
                return
            }
            shouldOverride = true
        }

        val targetUUID = this.moderation.system.userHandler.getUUID(args[0])
        val target = this.moderation.system.getOfflineUser(targetUUID).join() //We are already on another thread, so we can simply join this

        if(target == null) {
            sender.sendMessage(config.getString("$path.notExist"), PlaceHolder("prefix", config.getString("messages.prefix")), PlaceHolder("target", args[0]))
            return
        }

        val numberReason = args[1].toIntOrNull()
        val reason =
            if(numberReason == null)
                this.moderation.helper.getReason(args[1])
            else
                this.moderation.helper.getReason(numberReason)

        if(reason == null) {
            sender.sendMessage(config.getString("$path.reasonNotExist"), PlaceHolder("prefix", config.getString("messages.prefix")), PlaceHolder("reason", args[1]))
            return
        }

        val modID =
            if(sender is User)
                sender.id
            else
                (0).toLong()

        val history = historyHandler.getHistory(target.id).join()
        val count = history.filterReasons(reason).size + 1

        val countToGet =
            if(reason.length.containsKey(count))
                count
            else
                reason.length.size - 1

        val length = reason.length[countToGet]!!

        if(!shouldOverride) {

            if(reason.types[countToGet] == Type.BAN && this.banHandler.hasBan(target.id).join()) {

                if(!sender.hasPermission(moderation.system.commandFile.getString("ban.overridepermission")))
                    sender.sendMessage(config.getString("$path.alreadyPunished.banNoPermission"),
                        PlaceHolder("prefix", config.getString("messages.prefix")),
                        PlaceHolder("user", target.displayName),
                        PlaceHolder("button", config.getString("$path.alreadyPunished.button")))
                else
                    sender.sendMessage(config.getString("$path.alreadyPunished.ban"),
                        PlaceHolder("prefix", config.getString("messages.prefix")),
                        PlaceHolder("user", target.displayName),
                        PlaceHolder("button", config.getString("$path.alreadyPunished.button")))

                return
            } else if(this.banHandler.hasMute(target.id).join()) {

                if(!sender.hasPermission(moderation.system.commandFile.getString("ban.overridepermission")))
                    sender.sendMessage(config.getString("$path.alreadyPunished.muteNoPermission"),
                        PlaceHolder("prefix", config.getString("messages.prefix")),
                        PlaceHolder("user", target.displayName),
                        PlaceHolder("button", config.getString("$path.alreadyPunished.button")))
                else
                    sender.sendMessage(config.getString("$path.alreadyPunished.mute"),
                        PlaceHolder("prefix", config.getString("messages.prefix")),
                        PlaceHolder("user", target.displayName),
                        PlaceHolder("button", config.getString("$path.alreadyPunished.button")))

                return
            }

        }

        val entry = HistoryEntry(historyHandler.nextID, target.id, modID, count, reason, System.currentTimeMillis(), length, false)

        //create ban in the database
        this.banHandler.create(entry)
        this.historyHandler.saveEntry(entry)

        sender
    }

}