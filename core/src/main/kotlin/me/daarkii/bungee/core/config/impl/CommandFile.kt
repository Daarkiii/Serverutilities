package me.daarkii.bungee.core.config.impl

import me.daarkii.bungee.core.config.Config
import java.io.File

class CommandFile(dataFolder: File) : Config(File(dataFolder, "commands.yml"), "commands.yml") {

    /**
     * Gets called after the file is loaded
     */
    override fun afterLoad() {
    }
}