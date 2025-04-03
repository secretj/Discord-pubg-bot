package com.secretj.pubg_bot.domain.discord.command.application.event


import com.secretj.pubg_bot.domain.discord.command.domain.enum.DiscordRole
import com.secretj.pubg_bot.domain.discord.command.domain.user.application.UserRegistService
import com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request.PlayerStatusDTO
import com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request.ResistUserDTO
import com.secretj.pubg_bot.infrastructure.PubgApiService
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.`object`.entity.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MessageCommandEventHandler(
    private val userService: UserRegistService,
    private val pubgApiService: PubgApiService
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

    // 채팅으로 디스코드 ID PUBG ID를 입력받아서 등록하는 기능
    private fun handleRegisterCommand(message: Message): Mono<Void> {
        val parts = message.content.split(" ")
        if (parts.size != 2) {
            return message.channel
                .flatMap { it.createMessage("올바른 형식: !register <PUBG ID>") }
                .then()
        }

        val discordId = message.author.map { it.id.asString() } ?: return Mono.empty()
        val pubgId = parts[1]

        // 비동기 처리
        return Mono.fromCallable {
            userService.registUser(ResistUserDTO(discordId.toString(), pubgId))
        }
            .flatMap {
                message.channel.flatMap {
                    it.createMessage("성공적으로 등록되었습니다: $pubgId")
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

    private fun formatStatsMessage(stats: PlayerStatusDTO): String = """
        PUBG 플레이어 통계:
        - 딜: ${stats.deal}
        - 승리: ${stats.winRate}
        - 랭크: ${determineRank(stats)}
    """.trimIndent()

    private fun determineRank(stats: PlayerStatusDTO): String = when {
        stats.deal > 500 && stats.winRate > 50 -> DiscordRole.CHALLENGER
        stats.deal > 250 && stats.winRate > 20 -> DiscordRole.MASTER
        stats.deal > 100 && stats.winRate > 10 -> DiscordRole.EXTREME
        else -> DiscordRole.THE_FIRST
    }.toString()
}