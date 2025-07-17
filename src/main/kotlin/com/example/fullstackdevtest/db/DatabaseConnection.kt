package com.example.fullstackdevtest.db

import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import java.sql.Connection
import java.sql.DriverManager

@SpringBootConfiguration
class DatabaseConnection {
    private val url = "jdbc:postgresql://localhost:5432/product"
    private val username = "postgres"
    private val password = "1234"

    @Bean
    fun getDatabaseConnection(): Connection {
        return DriverManager.getConnection(url, username, password)
    }
}