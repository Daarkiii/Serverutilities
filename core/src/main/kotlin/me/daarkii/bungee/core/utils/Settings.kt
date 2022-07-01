package me.daarkii.bungee.core.utils

class Settings() {

    init {
        instance = this
    }

    /*
    Cloud Provider
     */

    var isCloudNetActive = false

    var isSimpleCloudActive = false

    /*
    Storage Provider
     */

    var useMongo = false

    var useMySQL = false

    companion object {
        @JvmStatic
        lateinit var instance: Settings
            private set
    }

}