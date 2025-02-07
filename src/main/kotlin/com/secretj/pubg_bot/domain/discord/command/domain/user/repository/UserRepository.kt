package com.secretj.pubg_bot.domain.discord.command.domain.user.repository

import com.secretj.pubg_bot.domain.discord.command.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>