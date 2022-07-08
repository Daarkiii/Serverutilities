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

package me.daarkii.addon.moderation.config

import me.daarkii.addon.moderation.Moderation
import me.daarkii.addon.moderation.`object`.Reason
import me.daarkii.addon.moderation.`object`.Type
import me.daarkii.bungee.core.config.Config
import java.io.File
import java.util.LinkedList
import java.util.concurrent.TimeUnit

class ReasonFile(private val moderation: Moderation) : Config(File(moderation.dataFolder, "reasons.yml"), "reasons.yml") {

    /**
     * Gets called after the file is loaded
     */
    override fun afterLoad() {

        val reasons: MutableList<Reason> = LinkedList()

        for(key in configuration.getKeys(false)) {

            val id = key.toIntOrNull() ?: continue

            val lengths: MutableMap<Int, Long> = LinkedHashMap()
            val types: MutableMap<Int, Type> = LinkedHashMap()
            val points: MutableMap<Int, Double> = LinkedHashMap()

            for(subKey in configuration.getConfigurationSection("$id.lengths").getKeys(false)) {

                val subID = subKey.toIntOrNull() ?: continue

                lengths[subID] = this.migrateToMillis(
                    configuration.getInt("$id.lengths.$subID.number"),
                    configuration.getString("$id.lengths.$subID.interval"))

                val type = Type.fromString(configuration.getString("$id.lengths.$subID.type")) ?: continue
                types[subID] = type

                points[subID] = configuration.getDouble("$id.lengths.$subID.points")
            }

            val reason = Reason(
                id,
                configuration.getString("$id.name"),
                configuration.getString("$id.display"),
                lengths,
                types,
                points
            )

            reasons.add(reason)
        }

        this.moderation.helper.setupReasons(reasons)
    }

    private fun migrateToMillis(number: Int, interval: String) : Long {

        var time = (0).toLong()

        when(interval) {
            "y" -> time = TimeUnit.DAYS.toMillis((number * 365).toLong())
            "m" -> time = TimeUnit.DAYS.toMillis((number * 30).toLong())
            "w" -> time = TimeUnit.DAYS.toMillis((number * 7).toLong())
            "d" -> time = TimeUnit.DAYS.toMillis(number.toLong())
            "h" -> time = TimeUnit.HOURS.toMillis(number.toLong())
            "mm" -> time = TimeUnit.MINUTES.toMillis(number.toLong())
            "s" -> time = TimeUnit.SECONDS.toMillis(number.toLong())
        }

        return time
    }
}