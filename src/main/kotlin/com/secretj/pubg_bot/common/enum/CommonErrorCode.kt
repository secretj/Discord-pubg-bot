package com.secretj.pubg_bot.common.enum

import com.secretj.pubg_bot.common.exception.ErrorCode


enum class CommonErrorCode(
    override val code: Int,
    override val message: String
) : ErrorCode {
    UNKNOWN_ERROR(1000, "알 수 없는 오류가 발생했습니다."),
    INVALID_PARAMETER(1001, "유효하지 않은 파라미터."),
    INTERNAL_SERVER_ERROR(1002, "서버 내부 오류가 발생했습니다.")
}