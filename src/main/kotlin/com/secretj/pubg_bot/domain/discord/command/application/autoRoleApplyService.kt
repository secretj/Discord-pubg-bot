package com.secretj.pubg_bot.domain.discord.command.application

import com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request.PlayerStatusDTO
import com.secretj.pubg_bot.domain.pubg.application.PubgApiService
import com.secretj.pubg_bot.domain.discord.command.domain.user.repository.UserRepository
import discord4j.core.GatewayDiscordClient
import discord4j.core.`object`.entity.Role
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AutoRoleApplyService(
    private val userRepository: UserRepository,
    private val pubgApiService: PubgApiService,
    private val discordClient: GatewayDiscordClient
) {
    fun applyRolesBasedOnStats() {
        val users = userRepository.findAll()

        users.forEach { user ->
            try {
                val playerStats = pubgApiService.getPlayerStats(user.pubgId)
                val role = determineRole(playerStats)

                assignRoleToUser(user.discordId, role)
            } catch (e: Exception) {
                // 로깅 또는 오류 처리
                println("Failed to process user ${user.pubgId}: ${e.message}")
            }
        }
    }

    private fun determineRole(stats: PlayerStatusDTO): String {
        return when {
            stats.deal > 500 && stats.winRate > 50 -> "Professional"
            stats.deal > 250 && stats.winRate > 20 -> "Advanced"
            stats.deal > 100 && stats.winRate > 10 -> "Intermediate"
            else -> "Beginner"
        }
    }

    private fun assignRoleToUser(discordId: String, roleName: String) {
        discordClient.guilds.flatMap { guild ->
            // 디스코드 서버에서 해당 이름의 역할 찾기
            guild.roles
                .filter { it.name == roleName }
                .next()
                .flatMap { role ->
                    // 사용자 찾아서 역할 부여
                    guild.members
                        .filter { it.id.asString() == discordId }
                        .next()
                        .flatMap { member ->
                            member.addRole(role.id)
                        }
                }
        }.subscribe()
    }
}