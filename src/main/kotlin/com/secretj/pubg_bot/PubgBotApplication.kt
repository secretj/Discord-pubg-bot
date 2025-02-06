package com.secretj.pubg_bot

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PubgBotApplication {
    init {
        // .env 파일 로드
        val dotenv = Dotenv.configure().load()
        dotenv.entries().forEach { entry ->
            System.setProperty(entry.key, entry.value)
        }
    }
}

fun main(args: Array<String>) {
	runApplication<PubgBotApplication>(*args)
}
