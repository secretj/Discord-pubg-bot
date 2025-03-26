package com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request

data class PlayerStatusDTO(
    val winRate: Long,
    val deal: Long,
    val rate: Int
) {
}