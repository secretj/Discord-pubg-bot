package com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request

import com.secretj.pubg_bot.domain.discord.command.domain.user.entity.User

data class ResistUserDTO(
    val discordId: String,
    val pubgId: String
) {
    constructor(request: Map<String, String>) : this(
        discordId = request["discordId"] ?: "",
        pubgId = request["pubgId"] ?: ""
    )

    fun toEntity() = User(
        discordId = discordId,
        pubgId = pubgId
    )
}