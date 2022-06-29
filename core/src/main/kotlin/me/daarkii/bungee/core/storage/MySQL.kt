package me.daarkii.bungee.core.storage

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.daarkii.bungee.core.config.Config
import javax.sql.DataSource

class MySQL(private val settings: Config) {

    private val hikariConfig: HikariConfig = HikariConfig()
    private var dataSourceObj: HikariDataSource? = null

    init {
        this.connect()
        this.createTables()
    }

    /**
     * Starts a new Datasource
     */
    private fun connect() {
        hikariConfig.jdbcUrl = "jdbc:mysql://" + settings.getString("mysql.host") + ":" + settings.getString("mysql.port")  + "/" + settings.getString("mysql.database")
        hikariConfig.username = settings.getString("mysql.user")
        hikariConfig.password = settings.getString("mysql.password")
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true")
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250")
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        hikariConfig.maximumPoolSize = settings.getInteger("mysql.maxConnections")
        dataSourceObj = HikariDataSource(hikariConfig)
    }

    /**
     * Creates all needed tables in the mysql database
     */
    private fun createTables() {

    }

    /**
     * @return the Datasource for connections
     */
    val dataSource: DataSource
        get() = dataSourceObj!!

}