package me.daarkii.bungee.core.utils

class Settings {

    init {
        instance = this
    }

    private var cloudNet = false
    private var simpleCloud = false

    /*
    Cloud Provider
     */

    fun activateCloudNet() {
        this.cloudNet = true
    }

    fun activateSimpleCloud() {
        this.simpleCloud = true
    }

    fun isCloudNetActive() : Boolean {
        return cloudNet
    }

    fun isSimpleCloudActive() : Boolean {
        return simpleCloud
    }

    companion object {
        @JvmStatic
        lateinit var instance: Settings
            private set
    }

}