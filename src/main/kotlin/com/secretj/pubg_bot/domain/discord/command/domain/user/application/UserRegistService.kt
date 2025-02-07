package com.secretj.pubg_bot.domain.discord.command.domain.user.application
import com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request.ResistUserDTO
import com.secretj.pubg_bot.domain.discord.command.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserRegistService(
    private val userRepository: UserRepository
) {
    fun registUser(resisterUserDTO : ResistUserDTO) {
        userRepository.save(resisterUserDTO.toEntity())
    }
}