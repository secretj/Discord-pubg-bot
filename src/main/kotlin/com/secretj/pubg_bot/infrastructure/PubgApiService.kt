package com.secretj.pubg_bot.infrastructure

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class PubgApiService(
    private val restTemplate: RestTemplate
) {
    private val apiKey = System.getenv("PUBG_API_KEY")
    private val baseUrl = "https://api.pubg.com/shards/steam"

    fun getPlayerStats(pubgId: String): PlayerStatsDTO {
        val playerUrl = "$baseUrl/players?filter[playerNames]=$pubgId"
        val playerResponse = restTemplate.getForObject<PlayerResponse>(playerUrl, apiKey)

        val playerId = playerResponse.data.first().id
        val statsUrl = "$baseUrl/players/$playerId/seasons/division.bro.official.pc-2018-01"

        val statsResponse = restTemplate.getForObject<StatsResponse>(statsUrl, apiKey)

        return convertToPlayerStatsDTO(statsResponse)
    }

    private fun convertToPlayerStatsDTO(statsResponse: StatsResponse): PlayerStatsDTO {
        // PUBG API 응답을 기반으로 PlayerStatsDTO 변환 로직
        val gameMode = statsResponse.data.attributes.gameMode
        return PlayerStatsDTO(
            kills = gameMode.solo.kills,
            wins = gameMode.solo.wins,
            // 다른 통계 추가
        )
    }
}

data class PlayerStatsDTO(
    val kills: Int,
    val wins: Int
    // 다른 통계 필드 추가
)

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
    val kills: Int,
    val wins: Int
    // 다른 통계 추가
)