package me.daarkii.bungee.core.config.impl.messages

import me.daarkii.bungee.core.config.Config
import java.io.File

class MessageFile(folder: File, configName: String) : Config(File(folder, "$configName.yml"), "messages/$configName.yml") {

    /**
     * Gets called after the file is loaded
     */
    override fun afterLoad() {
    }
}