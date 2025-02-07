package com.secretj.pubg_bot.infrastructure

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class MysqlConnector {

    private val dotenv = Dotenv.load()

    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver")
        dataSource.url = dotenv["DATABASE_URL"]
        dataSource.username = dotenv["DATABASE_USERNAME"]
        dataSource.password = dotenv["DATABASE_PASS"]
        return dataSource
    }
}