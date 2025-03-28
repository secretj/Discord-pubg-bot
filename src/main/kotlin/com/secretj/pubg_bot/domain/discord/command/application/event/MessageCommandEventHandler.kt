package com.secretj.pubg_bot.domain.discord.command.application.event


import com.secretj.pubg_bot.domain.discord.command.domain.enum.DiscordRole
import com.secretj.pubg_bot.domain.discord.command.domain.user.application.UserRegistService
import com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request.ResistUserDTO
import com.secretj.pubg_bot.infrastructure.PlayerStatsDTO
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.Message
import discord4j.core.spec.MessageCreateSpec
import discord4j.rest.service.UserService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MessageCommandEventHandler(
    private val userService: UserRegistService
) : DiscordEventHandler<MessageCreateEvent> {

    override fun eventType(): Class<MessageCreateEvent> = MessageCreateEvent::class.java

    override fun handle(event: MessageCreateEvent): Mono<Void> {
        val message = event.message
        val content = message.content.trim()

        return when {
            content.startsWith("!register") -> handleRegisterCommand(message)
            content.startsWith("!stats") -> handleStatsCommand(message)
            else -> Mono.empty()
        }
    }

    private fun handleRegisterCommand(message: Message): Mono<Void> {
        val parts = message.content.split(" ")
        if (parts.size != 3) {
            return message.channel
                .flatMap { it.createMessage("올바른 형식: !register <디스코드ID> <PUBG ID>") }
                .then()
        }

        val discordId = message.author.map { it.id.asString() }.block() ?: return Mono.empty()
        val pubgId = parts[2]


        return Mono.fromCallable {
            userService.registUser(ResistUserDTO(discordId, pubgId))
        }
            .flatMap {
                message.channel.flatMap {
                    it.createMessage("성공적으로 등록되었습니다: ${it.pubgId}")
                }
            }
            .onErrorResume { e ->
                message.channel.flatMap {
                    it.createMessage("등록 중 오류 발생: ${e.message}")
                }
            }
            .then()
    }

    private fun handleStatsCommand(message: Message): Mono<Void> {
        val parts = message.content.split(" ")
        val pubgId = if (parts.size > 1) parts[1] else null

        return Mono.fromCallable {
            pubgId?.let {
                pubgApiService.getPlayerStats(it)
            }
        }
            .flatMap { stats ->
                message.channel.flatMap {
                    it.createMessage(formatStatsMessage(stats))
                }
            }
            .onErrorResume { e ->
                message.channel.flatMap {
                    it.createMessage("통계 조회 중 오류 발생: ${e.message}")
                }
            }
            .then()
    }

    private fun formatStatsMessage(stats: PlayerStatsDTO): String = """
        PUBG 플레이어 통계:
        - 킬: ${stats.kills}
        - 승리: ${stats.wins}
        - 랭크: ${determineRank(stats)}
    """.trimIndent()

    private fun determineRank(stats: PlayerStatsDTO): String = when {
        stats.kills > 500 && stats.wins > 50 -> DiscordRole.CHALLENGER
        stats.kills > 250 && stats.wins > 20 -> DiscordRole.MASTER
        stats.kills > 100 && stats.wins > 10 -> DiscordRole.EXTREME
        else -> DiscordRole.THE_FIRST
    }.toString()
}