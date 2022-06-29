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
    private val info: AddonInfo
) {

    private val bungeeSystem = BungeeSystem.getInstance()
    private val classLoaderObject = this.javaClass.classLoader
    private val dataFolderObject = File(bungeeSystem.dataFolder.absolutePath + "/addons/" + name)

    open fun onStart() {
    }

    open fun onEnd() {
    }

    val dataFolder: File
        get() = dataFolderObject

    val name: String
        get() = info.name

    val system: BungeeSystem
        get() = bungeeSystem

    val classLoader: ClassLoader
        get() = classLoaderObject

    val addonInfo: AddonInfo
        get() = info

    fun loadFile(fileName: String) : File {
        return File(info.file.absolutePath + "!" + fileName)
    }

}
