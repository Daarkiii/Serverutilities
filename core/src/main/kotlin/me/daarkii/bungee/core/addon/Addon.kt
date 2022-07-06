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

package me.daarkii.bungee.core.addon

import me.daarkii.bungee.core.BungeeSystem
import java.io.File

/**
 * Models the main part of the addon system
 *
 * Addons will be mostly used to extend this base of plugin.
 * Addons main class must implement this addon class
 *
 *
 * Every addon must be placed in the corresponding addon folder of this plugin
 * This resource will then find the main class, which must implement the onStart method
 * and create a new instance of the class.
 */

abstract class Addon(
    val addonInfo: AddonInfo
) {

    /**
     * Main class of the BungeeUtilities plugin
     */
    val system = BungeeSystem.getInstance()

    /**
     * The classloader for the addon
     */
    val classLoader = this.javaClass.classLoader

    /**
     * The name of this addon
     */
    val name = addonInfo.name

    /**
     * The data-folder of this addon
     */
    val dataFolder = File(system.dataFolder.absolutePath + "/addons/" + name)

    open fun onStart() {
        //Startup logic here
    }

    open fun onEnd() {
        //Disable logic here
    }

    /**
     * Loads a file from the data-folder of the addon
     */
    fun loadFile(fileName: String) : File {
        return File(addonInfo.file.absolutePath + "!" + fileName)
    }

}
