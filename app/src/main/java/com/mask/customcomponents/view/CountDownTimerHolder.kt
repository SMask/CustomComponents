package com.mask.customcomponents.view

import android.os.CountDownTimer
import android.os.SystemClock

/**
 * 倒计时
 * 所有时间均为毫秒
 *
 * Create by lishilin on 2025-08-20
 */
class CountDownTimerHolder {

    private enum class Action {
        START, TICK, CANCEL, FINISH
    }

    companion object {
        const val MILLIS_SECOND = 1000L
        const val MILLIS_MINUTE = MILLIS_SECOND * 60
        const val MILLIS_HOUR = MILLIS_MINUTE * 60
        const val MILLIS_DAY = MILLIS_HOUR * 24
    }

    private var isRunning = false

    private var mStartTime: Long? = null // 开始时间
    private var mRemainingTimeForStart: Long? = null // 剩余时间（相对于开始时间，倒计时总时长）
    private var mCountDownInterval: Long = MILLIS_SECOND // 回调间隔时间

    private var mListener: CountDownTimerListener? = null

    private var mCountDownTimer: CountDownTimer? = null

    private fun startInternal() {
        if (isRunning) {
            return
        }
        val mStartTime = mStartTime ?: return
        val mRemainingTimeForStart = mRemainingTimeForStart ?: return
        val millisInFuture = getRemainingTime(mStartTime, mRemainingTimeForStart)
        isRunning = true
        dispatchAction(Action.START, mStartTime, mRemainingTimeForStart)
        mCountDownTimer = object : CountDownTimer(millisInFuture, mCountDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                dispatchAction(Action.TICK, mStartTime, mRemainingTimeForStart, millisUntilFinished)
            }

            override fun onFinish() {
                dispatchAction(Action.FINISH, mStartTime, mRemainingTimeForStart)
                this@CountDownTimerHolder.mCountDownTimer = null
                this@CountDownTimerHolder.mStartTime = null
                this@CountDownTimerHolder.mRemainingTimeForStart = null
            }
        }.start()
    }

    private fun cancelInternal() {
        if (!isRunning) {
            return
        }
        isRunning = false
        mCountDownTimer?.cancel()
        if (mCountDownTimer != null) {
            dispatchAction(Action.CANCEL, mStartTime, mRemainingTimeForStart)
        }
        mCountDownTimer = null
    }

    private fun getRemainingTime(startTime: Long, remainingTimeForStart: Long): Long {
        return (startTime + remainingTimeForStart - SystemClock.elapsedRealtime()).coerceAtLeast(0L)
    }

    private fun dispatchAction(
        action: Action,
        startTime: Long?,
        remainingTimeForStart: Long?,
        millisUntilFinished: Long? = null
    ) {
        val mListener = mListener ?: return
        if (startTime == null || remainingTimeForStart == null) {
            return
        }
        val remainingTime = millisUntilFinished ?: getRemainingTime(
            startTime, remainingTimeForStart
        )
        val countDownTimerInfo = createCountDownTimerInfo(
            startTime, remainingTimeForStart, remainingTime
        )
        when (action) {
            Action.START -> {
                mListener.onStart(countDownTimerInfo)
            }

            Action.TICK -> {
                mListener.onTick(countDownTimerInfo)
            }

            Action.CANCEL -> {
                mListener.onCancel(countDownTimerInfo)
            }

            Action.FINISH -> {
                mListener.onFinish(countDownTimerInfo)
            }
        }
    }

    private fun createCountDownTimerInfo(
        startTime: Long,
        remainingTimeForStart: Long,
        remainingTime: Long
    ): CountDownTimerInfo {
        val remainingDays = remainingTime / MILLIS_DAY
        val remainingHours = remainingTime % MILLIS_DAY / MILLIS_HOUR
        val remainingMinutes = remainingTime % MILLIS_HOUR / MILLIS_MINUTE
        val remainingSeconds = remainingTime % MILLIS_MINUTE / MILLIS_SECOND
        return CountDownTimerInfo(
            startTime,
            remainingTimeForStart,
            remainingTime,
            remainingDays,
            remainingHours,
            remainingMinutes,
            remainingSeconds
        )
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setListener(listener: CountDownTimerListener) {
        this.mListener = listener
    }

    /**
     * 设置时间
     * startTime 使用 SystemClock.elapsedRealtime()
     */
    fun setTime(
        startTime: Long,
        remainingTimeForStart: Long,
        countDownInterval: Long = MILLIS_SECOND,
        isStart: Boolean = true
    ) {
        cancelInternal()
        this.mStartTime = startTime
        this.mRemainingTimeForStart = remainingTimeForStart
        this.mCountDownInterval = countDownInterval
        if (isStart) {
            startInternal()
        }
    }

    fun start() {
        if (isRunning) {
            return
        }
        cancelInternal()
        startInternal()
    }

    fun cancel() {
        if (!isRunning) {
            return
        }
        cancelInternal()
    }

    /************************************************************ E 外部调用 ************************************************************/

}

data class CountDownTimerInfo(
    val startTime: Long, // 开始时间
    val remainingTimeForStart: Long, // 剩余时间（相对于开始时间，倒计时总时长）
    val remainingTime: Long, // 剩余时间（相对于结束时间，倒计时剩余时长）
    val remainingDays: Long, // 剩余天数
    val remainingHours: Long, // 剩余小时数
    val remainingMinutes: Long, // 剩余分钟数
    val remainingSeconds: Long // 剩余秒数
)

abstract class CountDownTimerListener {
    open fun onStart(info: CountDownTimerInfo) {
    }

    open fun onTick(info: CountDownTimerInfo) {
    }

    open fun onCancel(info: CountDownTimerInfo) {
    }

    open fun onFinish(info: CountDownTimerInfo) {
    }
}