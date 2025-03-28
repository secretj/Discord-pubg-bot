package com.secretj.pubg_bot.domain.discord.command.domain.config

import com.secretj.pubg_bot.domain.discord.command.application.event.DiscordEventHandler
import com.secretj.pubg_bot.domain.discord.command.application.event.DiscordEventRegistrar
import discord4j.core.DiscordClient
import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.Event
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordClientConfig {

    @Bean
    fun gatewayDiscordClient(dotenv: Dotenv): GatewayDiscordClient {
        val token = dotenv["DISCORD_BOT_TOKEN"]
            ?: throw IllegalStateException("Discord bot token not configured")

        return DiscordClient.create(token)
            .gateway()
            .login()
            .block() ?: throw IllegalStateException("Failed to create Discord gateway client")
    }

    @Bean
    fun discordEventListener(
        eventHandlers: List<DiscordEventHandler<out Event>>
    ): DiscordEventRegistrar {
        return DiscordEventRegistrar(eventHandlers)
    }
}