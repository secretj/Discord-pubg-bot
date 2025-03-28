package com.secretj.pubg_bot.common.config

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EnvConfig {
    @Bean
    fun dotenv(): Dotenv {
        return Dotenv.load()
    }
}