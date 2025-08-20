package com.mask.customcomponents.vo

import android.os.SystemClock

/**
 * 倒计时
 *
 * Create by lishilin on 2025-08-20
 */
data class CountDownTimerVo(
    val remainingTimeForStart: Long, // 剩余时间（相对于开始时间）
    val startTime: Long = SystemClock.elapsedRealtime(), // 开始时间
)
