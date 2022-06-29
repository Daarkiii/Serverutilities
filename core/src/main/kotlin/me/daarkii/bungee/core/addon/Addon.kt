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
 *
 * @author Devin
 * @version 1.0.0
 */

abstract class Addon(
    private val info: AddonInfo
) {

    private val name = info.getName()
    private val bungeeSystem = BungeeSystem.getInstance()
    private val classLoader = this.javaClass.classLoader
    private val dataFolder = File(bungeeSystem.getDataFolder().absolutePath + "/addons/" + name)

    open fun onStart() {
    }

    open fun onEnd() {
    }

    fun getDataFolder() : File {
        return dataFolder
    }

    fun getName() : String {
        return this.name
    }

    fun getSystem() : BungeeSystem {
        return bungeeSystem
    }

    fun getClassLoader() : ClassLoader {
        return classLoader
    }

    fun loadFile(fileName: String) : File {
        return File(info.getFile().absolutePath + "!" + fileName)
    }

    fun getAddonInfo() : AddonInfo {
        return info
    }

}
