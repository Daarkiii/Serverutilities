package me.daarkii.bungee.core.config

import org.simpleyaml.configuration.file.YamlFile
import java.io.File
import java.io.IOException
import java.nio.file.Files

abstract class Config(private val fileObj: File, private val location: String) {

    private val loader: ClassLoader = this.javaClass.classLoader
    private val nameObj: String = this.file.name
    private lateinit var configurationObj: YamlFile

    init {
        this.load()
    }

    /**
     * Gets called after the file is loaded
     */
    abstract fun afterLoad()

    /**
     * Gets the file from this Config
     *
     * @return this file for this config
     */
    val file: File
        get() = fileObj

    /**
     * Gets the Name of the Config
     *
     * @return the Name of the Config file
     */
    val name: String
        get() = nameObj

    /**
     * Gets the YamlFile of the Config
     *
     * @return the YamlFile of the Config
     */
    val configuration: YamlFile
        get() = configurationObj

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

        if(!fileObj.exists()) {

            //file does not exist
            fileObj.parentFile.mkdirs()

            //Debug
            println("Creating file ${fileObj.name}")

            kotlin.runCatching {
                loader.getResourceAsStream(location).use { stream ->

                    if(stream == null)
                        throw IOException("Can't create file $name")

                    Files.copy(stream, fileObj.toPath())
                }
            }.onFailure { e ->
                e.printStackTrace()
                return
            }
        }

        configurationObj = YamlFile(fileObj)

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