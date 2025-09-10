package com.mask.customcomponents.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView

/**
 * 倒计时
 *
 * Create by lishilin on 2025-08-20
 */
class CountDownTimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    // 是否对用户可见
    private var isVisibleToUser = false

    private val mCountDownTimerHolder by lazy {
        CountDownTimerHolder()
    }

    private var mProvider: CountDownTimerProvider = DefaultCountDownTimerProvider()

    private var mListener: CountDownTimerListener? = null

    init {
        mCountDownTimerHolder.setListener(object : CountDownTimerListener() {
            override fun onStart(info: CountDownTimerInfo) {
                super.onStart(info)
                setTimeText(info)
                mListener?.onStart(info)
            }

            override fun onTick(info: CountDownTimerInfo) {
                super.onTick(info)
                setTimeText(info)
                mListener?.onTick(info)
            }

            override fun onCancel(info: CountDownTimerInfo) {
                super.onCancel(info)
                setTimeText(info)
                mListener?.onCancel(info)
            }

            override fun onFinish(info: CountDownTimerInfo) {
                super.onFinish(info)
                setTimeText(info)
                mListener?.onFinish(info)
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isShown) {
            dispatchOnVisibleToUser(true)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dispatchOnVisibleToUser(false)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (isShown) {
            dispatchOnVisibleToUser(true)
        } else {
            dispatchOnVisibleToUser(false)
        }
    }

    private fun dispatchOnVisibleToUser(isVisibleToUser: Boolean) {
        if (this.isVisibleToUser == isVisibleToUser) {
            return
        }
        this.isVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            mCountDownTimerHolder.start()
        } else {
            mCountDownTimerHolder.cancel()
        }
    }

    private fun setTimeText(info: CountDownTimerInfo) {
        val timeText = mProvider.formatTime(info)
        val timePlaceholder = mProvider.formatTimePlaceholder(info)
        if (text != timeText) {
            text = timeText
        }
        if (hint != timePlaceholder) {
            hint = timePlaceholder
        }
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setProvider(provider: CountDownTimerProvider) {
        this.mProvider = provider
    }

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
        countDownInterval: Long = CountDownTimerHolder.MILLIS_SECOND,
        isStart: Boolean = true
    ) {
        mCountDownTimerHolder.setTime(
            startTime,
            remainingTimeForStart,
            countDownInterval,
            isStart && isVisibleToUser
        )
    }

    /************************************************************ E 外部调用 ************************************************************/

}

abstract class CountDownTimerProvider {

    /**
     * 格式化时间
     */
    abstract fun formatTime(info: CountDownTimerInfo): CharSequence

    /**
     * 格式化时间 占位
     * 解决部分机型绘制数字宽度不一致，导致依赖该控件的控件位置抖动问题。
     */
    abstract fun formatTimePlaceholder(info: CountDownTimerInfo): CharSequence
}

class DefaultCountDownTimerProvider : CountDownTimerProvider() {

    override fun formatTime(info: CountDownTimerInfo): CharSequence {
        val remainingDays = info.remainingDays
        val remainingDaysStr = remainingDays.toString()
        val remainingHoursStr = info.remainingHours.toString().padStart(2, '0')
        val remainingMinutesStr = info.remainingMinutes.toString().padStart(2, '0')
        val remainingSecondsStr = info.remainingSeconds.toString().padStart(2, '0')
        return if (remainingDays > 0) {
            "${remainingDaysStr}天${remainingHoursStr}:${remainingMinutesStr}:${remainingSecondsStr}"
        } else {
            "${remainingHoursStr}:${remainingMinutesStr}:${remainingSecondsStr}"
        }
    }

    override fun formatTimePlaceholder(info: CountDownTimerInfo): CharSequence {
        val remainingDays = info.remainingDays
        return if (remainingDays > 0) {
            val remainingDaysStr = "0".padEnd(remainingDays.toString().length, '0')
            "${remainingDaysStr}天00:00:00"
        } else {
            "00:00:00"
        }
    }
}