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

package me.daarkii.bungee.core.utils

import me.daarkii.bungee.core.config.impl.messages.Message
import net.kyori.adventure.text.Component

class PlaceHolder {

    var name: String = ""
    var component: Component = Component.text("")

    constructor(name: String, component: Component) {
        this.name = name
        this.component = component
    }

    constructor(name: String, value: String) {
        this.name = name

        if(value != "")
            this.component = Message.Wrapper.wrap(value)
    }

}