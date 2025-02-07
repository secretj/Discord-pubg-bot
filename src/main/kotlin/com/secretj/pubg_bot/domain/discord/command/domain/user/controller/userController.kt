package com.secretj.pubg_bot.domain.discord.command.domain.user.controller

import com.secretj.pubg_bot.domain.discord.command.domain.user.application.UserRegistService
import com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request.ResistUserDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping()
class UserController(private val userRegistService: UserRegistService) {

    @PostMapping("/user")
    fun registUser(@RequestBody request : ResistUserDTO) {
        userRegistService.registUser(request)
    }
}