package com.secretj.pubg_bot.common.scheduler

import org.springframework.scheduling.annotation.Scheduled
import com.secretj.pubg_bot.domain.discord.command.application.AutoRoleApplyService;
import org.springframework.stereotype.Component

@Component
class SchedulerService(
    private val autoRollService: AutoRoleApplyService
) {
    // 매주 월요일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * MON")
    fun cronUpdateUserRolls() {
        autoRollService.applyRolesBasedOnStats()
    }
}