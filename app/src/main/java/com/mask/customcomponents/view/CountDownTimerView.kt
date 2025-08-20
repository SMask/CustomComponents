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

    private var mProvider: CountDownTimerProvider = CountDownTimerProvider()

    private var mListener: CountDownTimerListener? = null

    init {
        mCountDownTimerHolder.setListener(object : CountDownTimerListener() {
            override fun onStart(info: CountDownTimerInfo) {
                super.onStart(info)
                text = mProvider.formatTime(info)
                mListener?.onStart(info)
            }

            override fun onTick(info: CountDownTimerInfo) {
                super.onTick(info)
                text = mProvider.formatTime(info)
                mListener?.onTick(info)
            }

            override fun onCancel(info: CountDownTimerInfo) {
                super.onCancel(info)
                text = mProvider.formatTime(info)
                mListener?.onCancel(info)
            }

            override fun onFinish(info: CountDownTimerInfo) {
                super.onFinish(info)
                text = mProvider.formatTime(info)
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
    fun setTime(startTime: Long, remainingTimeForStart: Long) {
        mCountDownTimerHolder.setTime(startTime, remainingTimeForStart, false)
    }

    /************************************************************ E 外部调用 ************************************************************/

}

open class CountDownTimerProvider {
    open fun formatTime(info: CountDownTimerInfo): CharSequence {
        val remainingDays = info.remainingDays
        val remainingDaysStr = remainingDays.toString()
        val remainingHoursStr = info.remainingHours.toString().padStart(2, '0')
        val remainingMinutesStr = info.remainingMinutes.toString().padStart(2, '0')
        val remainingSecondsStr = info.remainingSeconds.toString().padStart(2, '0')
        return if (remainingDays > 0) {
            "${remainingDaysStr}天${remainingHoursStr}:${remainingMinutesStr}"
        } else {
            "${remainingHoursStr}:${remainingMinutesStr}:${remainingSecondsStr}"
        }
    }
}