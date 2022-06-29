package me.daarkii.bungee.core.config.impl.messages

import me.daarkii.bungee.core.config.Config
import java.io.File

class Messages(private val name: String, private val dataFolder: File) {

    private lateinit var config: Config

    init {
        this.load()
        instance = this
    }

    private fun load() {
        val folder = File(dataFolder, "messages")
        folder.mkdirs()
        config = MessageFile(folder, name)
    }

    val prefix: String
        get() = config.getString("messages.prefix")

    val noPerms: String
        get() = config.getString("messages.user.noperms").replace("[Prefix]", prefix)

    companion object {
        @JvmStatic
        lateinit var instance: Messages
            private set
    }
}