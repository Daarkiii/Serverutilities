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
import org.simpleyaml.configuration.file.YamlFile
import java.io.File
import java.net.URLClassLoader

class AddonHandler(private val bungeeSystem: BungeeSystem) {

    val addons: MutableSet<Addon> = HashSet()

    private val dataFolder = bungeeSystem.dataFolder
    private val addonDirectory = loadAddonDirectory()

    private fun loadAddonDirectory() : File {
        val addonDirectory = File(dataFolder.absolutePath + "/addons")
        addonDirectory.mkdirs()
        return addonDirectory
    }

    fun loadAddons() {

        if(!addonDirectory.isDirectory) {
            bungeeSystem.logger.sendError("Addon-directory should not be a file")
            return
        }

        addonDirectory.listFiles()?.iterator()?.forEach {  file ->
            if(file.name.endsWith(".jar"))
                loadAddon(file)
        }

    }

    private fun loadAddon(file: File) {

        val loader = this.getClassLoader(file)
        val yamlFile = this.getYamlFile(loader, file) ?: return

        val main = yamlFile.getString("main")
        val name = yamlFile.getString("name")
        var author = yamlFile.getString("author")
        var version = yamlFile.getString("version")
        val depends : MutableList<String> = yamlFile.getStringList("depends")

        if(addons.stream().anyMatch { addon -> addon.addonInfo.name.equals(name, ignoreCase = true) }) {
            bungeeSystem.logger.sendError("Addon $name is already loaded")
            return
        }

        if(depends.contains(name)) {
            bungeeSystem.logger.sendError("Addon $name depends on it self")
            return
        }

        for(dependency in depends) {

            if(addons.stream().anyMatch { addon -> addon.addonInfo.name.equals(dependency, ignoreCase = true) })
                continue

            if(!loadDepend(dependency)) {
                bungeeSystem.logger.sendError("Can't find depend $dependency")
                return
            }
        }

        if(version == null)
            version = "N/A"

        if(author == null)
            author = "Unknown"

        if(main == null) {
            bungeeSystem.logger.sendError("Addon $name has no Main Class")
            return
        }

        var cls: Class<*>? = null

        kotlin.runCatching {
            cls = loader.loadClass(main)
        }.onFailure {
            bungeeSystem.logger.sendError("Can't load Main Class from addon $name")
            it.printStackTrace()
            return
        }

        if(cls?.superclass == null || cls?.superclass != Addon::class.java) {
            bungeeSystem.logger.sendError("Main Class from addon $name is not extending the Addon class")
            return
        }

        if(cls?.constructors?.size != 1) {
            bungeeSystem.logger.sendError("Main Class from addon $name has not an Constructor with the Addoninfo")
            return
        }

        var classObject: Any? = null
        val addonInfo = AddonInfo(name, version, author, main, file)

        kotlin.runCatching {
            classObject = cls?.constructors?.get(0)?.newInstance(addonInfo)
        }.onFailure {
            bungeeSystem.logger.sendError("Failed loading Addon $name")
            it.printStackTrace()
            return
        }

        bungeeSystem.logger.sendMessage("Enabled addon $name $version")
        val addon = classObject as Addon

        addon.onStart()
        addons.add(addon)
    }

    private fun getClassLoader(file: File) : ClassLoader {
        return URLClassLoader(arrayOf( file.toURI().toURL()), this.javaClass.classLoader)
    }

    private fun loadDepend(name: String) : Boolean {

        for(file in addonDirectory.listFiles()!!) {

            if(file.isDirectory)
                continue

            if(!file.name.endsWith(".jar"))
                continue

            val loader = this.getClassLoader(file)
            val yamlFile = this.getYamlFile(loader, file) ?: continue

            if(yamlFile.getString("name").equals(name, ignoreCase = true)) {
                this.loadAddon(file)
                return true
            }
        }

        return false
    }

    private fun getYamlFile(loader: ClassLoader, file: File) : YamlFile? {

        val stream = loader.getResourceAsStream("addon.yml")

        if(stream == null) {
            bungeeSystem.logger.sendError("Addon.yml was not found for file " + file.name)
            return null
        }

        val yamlConfig = YamlFile()

        kotlin.runCatching {
            yamlConfig.load(stream)
        }.onFailure {
            bungeeSystem.logger.sendError("Addon.yml can not be loaded for file " + file.name)
            it.printStackTrace()
            return null
        }

        return yamlConfig
    }

}