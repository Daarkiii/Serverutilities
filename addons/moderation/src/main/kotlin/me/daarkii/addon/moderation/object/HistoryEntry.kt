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

package me.daarkii.addon.moderation.`object`

import me.daarkii.addon.moderation.Moderation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

data class HistoryEntry(
    val id: Int,
    val owner: Long,
    val mod: Long, //if the console is the mod the id is 0
    val count: Int,
    val reason: Reason?,
    val start: Long,
    val end: Long,
    var unBanned: Boolean
) {

    /**
     * Gets the type of the reason
     */
    val type = reason?.types?.get(count) ?: Type.BAN

    /**
     * Gets the date where the punishment was created
     */
    val startDate = Date(start)

    /**
     * Gets the date where the punishment will end
     */
    val endDate = Date(end)

    /**
     * Formats the date where the punishment was created
     */
    val formattedStartDate: String
        get() {
            if(this.start == (-1).toLong())
                return "-1"

            val format = SimpleDateFormat(Moderation.instance.config.getString("timeFormat"))
            return format.format(this.startDate)
        }

    /**
     * Formats the date where the punishment will end
     */
    val endDateFormatted: String
        get() {
            if(this.end == (-1).toLong())
                return "-1"

            val format = SimpleDateFormat(Moderation.instance.config.getString("timeFormat"))
            return format.format(this.endDate)
        }

}