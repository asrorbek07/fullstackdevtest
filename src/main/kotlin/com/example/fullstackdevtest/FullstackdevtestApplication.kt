package com.example.fullstackdevtest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FullstackdevtestApplication

fun main(args: Array<String>) {
    runApplication<FullstackdevtestApplication>(*args)
}
