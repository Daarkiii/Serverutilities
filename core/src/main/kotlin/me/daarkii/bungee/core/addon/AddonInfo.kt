package me.daarkii.bungee.core.addon

import java.io.File

data class AddonInfo(
    private val nameObj: String,
    private val versionObj: String,
    private val authorObj: String,
    private val mainObj: String,
    private val fileObj: File
) {

    val file: File
        get() = fileObj

    val main: String
        get() = mainObj

    val author: String
        get() = authorObj

    val version: String
        get() = versionObj

    val name: String
        get() = nameObj

}