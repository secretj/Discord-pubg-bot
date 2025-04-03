package com.secretj.pubg_bot

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PubgBotApplication {}

fun main(args: Array<String>) {
	runApplication<PubgBotApplication>(*args)
}
