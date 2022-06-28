package me.daarkii.bungee.core.config

import org.simpleyaml.configuration.file.YamlFile
import java.io.File
import java.io.IOException
import java.nio.file.Files

abstract class Config(private val file: File, private val location: String) {

    private val loader: ClassLoader = this.javaClass.classLoader
    private val name: String = this.file.name
    private lateinit var configuration: YamlFile

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
    fun getFile() : File {
        return file
    }

    /**
     * Gets the Name of the Config
     *
     * @return the Name of the Config file
     */
    fun getName() : String {
        return name
    }

    /**
     * Gets the YamlFile of the Config
     *
     * @return the YamlFile of the Config
     */
    fun getConfiguration() : YamlFile {
        return configuration
    }

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

        if(!file.exists()) {

            //file does not exist
            file.parentFile.mkdirs()

            //Debug
            println("Creating file $file")

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

        this.afterLoad()
    }

}