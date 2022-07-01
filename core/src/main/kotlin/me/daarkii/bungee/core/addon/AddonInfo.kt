package me.daarkii.bungee.core.addon

import java.io.File

data class AddonInfo(
    val name: String,
    val version: String,
    val author: String,
    val main: String,
    val file: File
)
