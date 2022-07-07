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

import me.daarkii.bungee.core.config.Config
import java.io.File

class ConfigFile(dataFolder: File) : Config(File(dataFolder, "config.yml"), "config.yml") {

    /**
     * Gets called after the file is loaded
     */
    override fun afterLoad() {
        TODO("Not yet implemented")
    }

}