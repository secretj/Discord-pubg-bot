package com.secretj.pubg_bot.domain.discord.command.application.event

import discord4j.core.GatewayDiscordClient
import discord4j.core.event.domain.Event
import reactor.core.publisher.Mono

interface DiscordEventHandler<T : Event> {
    fun eventType(): Class<T>
    fun handle(event: T): Mono<Void>
}

class DiscordEventRegistrar(
    private val eventHandlers: List<DiscordEventHandler<out Event>>
) {
    fun registerEventHandlers(gatewayDiscordClient: GatewayDiscordClient) {
        eventHandlers.forEach { handler ->
            gatewayDiscordClient.on(handler.eventType())
                .flatMap { event ->
                    @Suppress("UNCHECKED_CAST")
                    (handler as DiscordEventHandler<Event>).handle(event)
                }
                .subscribe()
        }
    }
}