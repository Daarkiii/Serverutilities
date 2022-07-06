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

package me.daarkii.bungee.core.config.impl

import me.daarkii.bungee.core.config.Config
import me.daarkii.bungee.core.config.impl.messages.Message
import java.io.File

class SettingFile(dataFolder: File) : Config(File(dataFolder, "settings.yml"), "settings.yml") {

    /**
     * Gets called after the file is loaded
     */
    override fun afterLoad() {
    }

}