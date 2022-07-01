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
