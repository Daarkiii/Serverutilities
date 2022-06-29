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

    var isCloudNetActive: Boolean
        get() = cloudNet
        set(value) { cloudNet = value}

    var isSimpleCloudActive: Boolean
        get() = simpleCloud
        set(value) { simpleCloud = value }

    companion object {
        @JvmStatic
        lateinit var instance: Settings
            private set
    }

}