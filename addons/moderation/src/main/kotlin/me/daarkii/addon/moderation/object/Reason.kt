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

data class Reason(
    val id: Int,
    val name: String,
    val display: String,
    val length: Map<Int, Long>,
    val types: Map<Int, Type>,
    val points: Map<Int, Double>
) {

    override fun equals(other: Any?): Boolean {

        if(other == null)
            return false

        if(this === other)
            return true

        if(other !is Reason)
            return false

        return other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}