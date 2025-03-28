package com.secretj.pubg_bot.common.exception

/**
 * 공통 에러 코드 : 1000 ~ 1100
 * 사용자 에러 코드 : 1101 ~ 1200
 *
 * @property code 에러 코드
 * @property message 에러 메시지
 */
interface ErrorCode {
    val code: Int
    val message: String
}