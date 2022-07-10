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

package me.daarkii.addon.moderation.handler

import me.daarkii.addon.moderation.Moderation
import me.daarkii.addon.moderation.`object`.HistoryEntry
import me.daarkii.addon.moderation.`object`.Reason
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

class Helper {

    private val reasons: MutableCollection<Reason> = ArrayList()

    /**
     * registers every set upped reason from the config
     */
    fun setupReasons(reasons: List<Reason>) {
        this.reasons.addAll(reasons)
    }

    /**
     * Gets a Reason with the given id
     *
     * @param id the id of the reason
     * @return the reason if exists
     */
    fun getReason(id: Int) : Reason? {
        return reasons.stream().filter { it.id == id }.collect(Collectors.toList()).firstOrNull()
    }

    /**
     * Gets a Reason with the name
     *
     * @param name the name of the reason
     * @return the reason if exists
     */
    fun getReason(name: String) : Reason? {
        return reasons.stream().filter { it.name == name }.collect(Collectors.toList()).firstOrNull()
    }

    /**
     * Gets the string with the ban screen which is displayed when a player got banned or
     * when a banned player joins
     *
     * @param entry the HistoryEntry for the ban screen
     * @return the ban screen
     */
    fun getBanScreen(entry: HistoryEntry) : String {
        val config = Moderation.instance.messages
        val path = "messages.punish.screen.banned.default"

        val banDays = TimeUnit.MILLISECONDS.toDays(entry.end - entry.start)
        val reason = entry.reason?.display ?: "Unknown"
        val remainingTime = this.getRemainingTimeFormatted(entry.end)

        return config.getString(config.getString(path)
            .replace("<remaining>", remainingTime)
            .replace("<reason>", reason)
            .replace("<days>", banDays.toString()))
    }

    /**
     * Gets the remaining time of a ban and migrates the long values to a string with the format out of the config
     *
     * @param end the end timestamp of the ban
     * @return the formatted time
     */
    private fun getRemainingTimeFormatted(end: Long) : String {

        val config = Moderation.instance.config
        val messages = Moderation.instance.messages

        if(end == (-1).toLong())
            return config.getString("time.permanent")

        var minutes = TimeUnit.MILLISECONDS.toMinutes(end - System.currentTimeMillis())
        var hours = 0
        var days = 0

        while (minutes > 59) {
            minutes -= 60
            hours += 1
        }

        while (hours > 23) {
            hours -= 24
            days += 1
        }

        val format = messages.getString("messages.punish.screen.banned.format")
        val builder = StringBuilder()

        //add days
        if(days == 1)
            builder.append(format
                .replace("<value>", days.toString())
                .replace("<interval>", config.getString("time.day")) + ", ")
        else
            builder.append(format
                .replace("<value>", days.toString())
                .replace("<interval>", config.getString("time.days")) + ", ")

        //add hours
        if(hours == 1)
            builder.append(format
                .replace("<value>", hours.toString())
                .replace("<interval>", config.getString("time.hour")) + ", ")
        else
            builder.append(format
                .replace("<value>", hours.toString())
                .replace("<interval>", config.getString("time.hours")) + ", ")

        //add minutes
        if(minutes.toInt() == 1)
            builder.append(format
                .replace("<value>", minutes.toString())
                .replace("<interval>", config.getString("time.minute")))
        else
            builder.append(format
                .replace("<value>", minutes.toString())
                .replace("<interval>", config.getString("time.minutes")))

        return builder.toString()
    }
}