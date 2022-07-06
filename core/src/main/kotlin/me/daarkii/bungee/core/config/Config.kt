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

package me.daarkii.bungee.core.config

import org.simpleyaml.configuration.file.YamlFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.Collections

abstract class Config(val file: File, private val location: String) {

    private val loader: ClassLoader = this.javaClass.classLoader

    /**
     * Gets the Name of the Config
     *
     * @return the Name of the Config file
     */
    val name: String = this.file.name

    /**
     * Gets the YamlFile of the Config
     *
     * @return the YamlFile of the Config
     */
    lateinit var configuration: YamlFile

    init {
        this.load()
    }

    /**
     * Gets called after the file is loaded
     */
    abstract fun afterLoad()

    /**
     * Gets a String from the Config with replaced Color codes
     *
     * @param path the Key for the string
     * @return the loaded String
     */
    fun getString(path: String) : String {
        return this.configuration.getString(path)
    }

    /**
     * Gets a List<String> from the Config
     *
     * @param path the Key for the string
     * @return the loaded List
     */
    fun getStringList(path: String) : MutableList<String> {
        return if(this.configuration.contains(path))
            this.configuration.getStringList(path)
        else
            Collections.emptyList()
    }

    /**
     * Gets an Integer from the Config
     *
     * @param path the Key for the Integer
     * @return the loaded Integer
     */
    fun getInteger(path: String) : Int {
        return this.configuration.getInt(path)
    }

    /**
     * Gets a Boolean from the Config
     *
     * @param path the Key for the Boolean
     * @return the loaded Boolean
     */
    fun getBoolean(path: String) : Boolean {
        return this.configuration.getBoolean(path)
    }

    /**
     * Gets a List from the Config
     *
     * @param path the Key for the List
     * @return the loaded List
     */
    fun getList(path: String) : List<*> {
        return this.configuration.getList(path)
    }

    /**
     * Saves current configuration to the file
     */
    fun save() {
        configuration.save(file)
    }

    /**
     * Load and create the file
     */
    private fun load() {

        if(!file.exists()) {

            //file does not exist
            file.parentFile.mkdirs()

            //Debug
            println("Creating file ${file.name}")

            kotlin.runCatching {
                loader.getResourceAsStream(location).use { stream ->

                    if(stream == null)
                        throw IOException("Can't create file $name")

                    Files.copy(stream, file.toPath())
                }
            }.onFailure { e ->
                e.printStackTrace()
                return
            }
        }

        configuration = YamlFile(file)

        kotlin.runCatching {
            configuration.loadWithComments()
        }.onFailure { e ->
            e.printStackTrace()
            return
        }

        /**
         * Update values
         */

        val newYamlFile = YamlFile()
        newYamlFile.load(javaClass.classLoader.getResourceAsStream(location))

        for(key in configuration.getKeys(true)) {
            if(!newYamlFile.contains(key))
                configuration.set(key, null)
        }

        for(key in newYamlFile.getKeys(true)) {
            if(!configuration.contains(key))
                configuration.set(key, newYamlFile.get(key))
        }

        save()
        this.afterLoad()
    }

}