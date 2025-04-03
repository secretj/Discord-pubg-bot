package com.secretj.pubg_bot.infrastructure

import com.secretj.pubg_bot.domain.discord.command.domain.user.dto.request.PlayerStatusDTO
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class PubgApiService(
    private val restTemplate: RestTemplate
) {
    private val apiKey = System.getenv("PUBG_API_KEY")
    private val baseUrl = "https://api.pubg.com/shards/steam"

    fun getPlayerStats(pubgId: String): PlayerStatusDTO {
        val playerUrl = "$baseUrl/players?filter[playerNames]=$pubgId"
        val playerResponse = restTemplate.getForObject<PlayerResponse>(playerUrl, apiKey)

        val playerId = playerResponse.data.first().id
        val statsUrl = "$baseUrl/players/$playerId/seasons/division.bro.official.pc-2018-01"

        val statsResponse = restTemplate.getForObject<StatsResponse>(statsUrl, apiKey)

        return convertToPlayerStatsDTO(statsResponse)
    }

    private fun convertToPlayerStatsDTO(statsResponse: StatsResponse): PlayerStatusDTO {
        // PUBG API 응답을 기반으로 PlayerStatsDTO 변환 로직
        val gameMode = statsResponse.data.attributes.gameMode
        return PlayerStatusDTO(
            gameMode.solo.wins,
            gameMode.solo.kills,
            gameMode.solo.rate,
            // 다른 통계 추가
        )
    }
}

// PUBG API 응답 데이터 구조에 맞는 데이터 클래스
data class PlayerResponse(val data: List<PlayerData>)
data class PlayerData(val id: String)
data class StatsResponse(val data: StatsData)
data class StatsData(
    val attributes: GameModeAttributes
)
data class GameModeAttributes(
    val gameMode: GameModeStats
)
data class GameModeStats(
    val solo: ModeStats
)
data class ModeStats(
    val kills: Long,
    val wins: Long,
    val rate: Int
    // 다른 통계 추가
)