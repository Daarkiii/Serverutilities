package me.daarkii.bungee.core.config.impl

import me.daarkii.bungee.core.config.Config
import java.io.File

class SettingFile(dataFolder: File) : Config(File(dataFolder, "settings.yml"), "settings.yml") {

    /**
     * Gets called after the file is loaded
     */
    override fun afterLoad() {
    }

}