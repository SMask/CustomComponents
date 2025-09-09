package com.mask.customcomponents.view

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.animation.addListener
import java.text.NumberFormat
import java.util.Locale

/**
 * 数字动画
 *
 * Create by lishilin on 2025-08-13
 */
class NumberTextAnimator {

    private val animDuration = 2000L

    private val animator by lazy {
        ValueAnimator().apply {
            interpolator = DecelerateInterpolator()
            duration = animDuration
        }
    }

    private val longTypeEvaluator by lazy {
        object : TypeEvaluator<Long> {
            override fun evaluate(fraction: Float, startValue: Long?, endValue: Long?): Long? {
                if (startValue == null || endValue == null) {
                    return 0
                }
                return (startValue + fraction * (endValue - startValue)).toLong()
            }
        }
    }

    private val attachStateChangeListener by lazy {
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}

            override fun onViewDetachedFromWindow(v: View) {
                animator.cancel()
            }
        }
    }

    private val numberFormat by lazy {
        NumberFormat.getNumberInstance(Locale.US)
    }

    private var startValue: Long? = null

    private fun startAnim(tvContent: TextView, targetValue: Long) {
        if (startValue == null) {
            startValue = targetValue
        }
        if (startValue == targetValue) {
            setText(tvContent, targetValue)
            return
        }

        animator.cancel()
        animator.addUpdateListener {
            val currentValue = it.animatedValue as? Long ?: targetValue
            setText(tvContent, currentValue)
        }
        animator.addListener(
            onEnd = {
                setText(tvContent, targetValue)
                tvContent.removeOnAttachStateChangeListener(attachStateChangeListener)
                animator.removeAllUpdateListeners()
                animator.removeAllListeners()
            }
        )
        animator.setObjectValues(startValue, targetValue)
        animator.setEvaluator(longTypeEvaluator)
        animator.start()

        tvContent.addOnAttachStateChangeListener(attachStateChangeListener)

        startValue = targetValue
    }

    private fun setText(tvContent: TextView, currentValue: Long) {
        tvContent.text = numberFormat.format(currentValue)
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