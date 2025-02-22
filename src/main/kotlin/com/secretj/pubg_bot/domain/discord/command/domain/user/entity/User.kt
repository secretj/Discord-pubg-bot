package com.secretj.pubg_bot.domain.discord.command.domain.user.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    val discordId: String = "",
    val pubgId: String = ""
)