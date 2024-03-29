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

package me.daarkii.bungee.core.`object`

import net.kyori.adventure.text.Component

interface User : CommandSender, GenericUser {

    fun connect(server: String)

    fun kick(reason: String)

    fun sendTabList(header: Component, footer: Component)

    val address: String

    val serverName: String

    val ping: Int

    val highestGroup: Group

    val groups: List<Group>

}