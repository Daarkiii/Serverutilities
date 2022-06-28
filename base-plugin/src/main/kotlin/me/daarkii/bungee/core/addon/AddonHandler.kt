package me.daarkii.bungee.core.addon

import me.daarkii.bungee.core.BungeeSystem
import org.simpleyaml.configuration.file.YamlFile
import java.io.File
import java.net.URLClassLoader

class AddonHandler(private val bungeeSystem: BungeeSystem) {

    private val addons: MutableSet<Addon> = LinkedHashSet()
    private val dataFolder = bungeeSystem.getDataFolder()
    private val addonDirectory = loadAddonDirectory()

    private fun loadAddonDirectory() : File {
        val addonDirectory = File(dataFolder.absolutePath + "/addons")
        addonDirectory.mkdirs()
        return addonDirectory
    }

    fun loadAddons() {

        if(!addonDirectory.isDirectory) {
            bungeeSystem.getLogger().sendError("Addondirectory should not be a file")
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

        if(addons.stream().anyMatch { addon -> addon.getAddonInfo().getName().equals(name, ignoreCase = true) }) {
            bungeeSystem.getLogger().sendError("Addon $name is already loaded")
            return
        }

        if(depends.contains(name)) {
            bungeeSystem.getLogger().sendError("Addon $name depends on it self")
            return
        }

        for(dependency in depends) {

            if(addons.stream().anyMatch { addon -> addon.getAddonInfo().getName().equals(dependency, ignoreCase = true) })
                continue

            if(!loadDepend(dependency)) {
                bungeeSystem.getLogger().sendError("Can't find depend $dependency")
                return
            }
        }

        if(version == null)
            version = "N/A"

        if(author == null)
            author = "Unknown"

        if(main == null) {
            bungeeSystem.getLogger().sendError("Addon $name has no Main Class")
            return
        }

        var cls: Class<*>? = null

        kotlin.runCatching {
            cls = loader.loadClass(main)
        }.onFailure {
            bungeeSystem.getLogger().sendError("Can't load Main Class from addon $name")
            it.printStackTrace()
            return
        }

        if(cls?.superclass == null || cls?.superclass != Addon::class.java) {
            bungeeSystem.getLogger().sendError("Main Class from addon $name is not extending the Addon class")
            return
        }

        if(cls?.constructors?.size != 1) {
            bungeeSystem.getLogger().sendError("Main Class from addon $name has not an Constructor with the Addoninfo")
            return
        }

        var classObject: Any? = null
        val addonInfo = AddonInfo(name, version, author, main, file)

        kotlin.runCatching {
            classObject = cls?.constructors?.get(0)?.newInstance(addonInfo)
        }.onFailure {
            bungeeSystem.getLogger().sendError("Failed loading Addon $name")
            it.printStackTrace()
            return
        }

        bungeeSystem.getLogger().sendMessage("Enabled addon $name $version")
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
            bungeeSystem.getLogger().sendError("Addon.yml was not found for file " + file.name)
            return null
        }

        val yamlConfig = YamlFile()

        kotlin.runCatching {
            yamlConfig.load(stream)
        }.onFailure {
            bungeeSystem.getLogger().sendError("Addon.yml can not be loaded for file " + file.name)
            it.printStackTrace()
            return null
        }

        return yamlConfig
    }

}