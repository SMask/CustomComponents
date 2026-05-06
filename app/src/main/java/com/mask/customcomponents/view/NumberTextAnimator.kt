package com.mask.customcomponents.view

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import java.text.NumberFormat
import java.util.Locale

/**
 * 数字动画
 *
 * Create by lishilin on 2025-08-13
 */
class NumberTextAnimator {

    private val mAnimDuration = 2000L

    private val mAnimator by lazy {
        ValueAnimator().apply {
            interpolator = DecelerateInterpolator()
            duration = mAnimDuration
        }
    }

    private val mLongTypeEvaluator by lazy {
        object : TypeEvaluator<Long> {
            override fun evaluate(fraction: Float, startValue: Long?, endValue: Long?): Long? {
                if (startValue == null || endValue == null) {
                    return 0
                }
                return (startValue + fraction * (endValue - startValue)).toLong()
            }
        }
    }

    private val mAttachStateChangeListener by lazy {
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}

            override fun onViewDetachedFromWindow(v: View) {
                mAnimator.cancel()
            }
        }
    }

    private val mNumberFormat by lazy {
        NumberFormat.getNumberInstance(Locale.US)
    }

    private var mStartValue: Long? = null

    private fun startAnim(tvContent: TextView, targetValue: Long) {
        val startValue = mStartValue ?: targetValue
        this.mStartValue = targetValue

        if (startValue == targetValue || !tvContent.isVisible) {
            setText(tvContent, targetValue)
            return
        }

        mAnimator.cancel()
        mAnimator.addUpdateListener {
            val currentValue = it.animatedValue as? Long ?: targetValue
            setText(tvContent, currentValue)
        }
        mAnimator.addListener(
            onEnd = {
                setText(tvContent, targetValue)
                tvContent.removeOnAttachStateChangeListener(mAttachStateChangeListener)
                mAnimator.removeAllUpdateListeners()
                mAnimator.removeAllListeners()
            }
        )
        mAnimator.setObjectValues(startValue, targetValue)
        mAnimator.setEvaluator(mLongTypeEvaluator)
        mAnimator.start()

        tvContent.addOnAttachStateChangeListener(mAttachStateChangeListener)
    }

    private fun setText(tvContent: TextView, currentValue: Long) {
        tvContent.text = mNumberFormat.format(currentValue)
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun start(tvContent: TextView, targetValue: Long?) {
        if (targetValue == null) {
            return
        }
        startAnim(tvContent, targetValue)
    }

    /************************************************************ E 外部调用 ************************************************************/

}