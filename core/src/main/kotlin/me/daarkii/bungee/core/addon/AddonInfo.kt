package me.daarkii.bungee.core.addon

import java.io.File

data class AddonInfo(
    private val name: String,
    private val version: String,
    private val author: String,
    private val main: String,
    private val file: File
) {

    fun getFile() : File {
        return this.file
    }

    fun getMain() : String {
        return this.main
    }

    fun getAuthor() : String {
        return this.author
    }

    fun getVersion() : String {
        return this.version
    }

    fun getName() : String {
        return this.name
    }

}