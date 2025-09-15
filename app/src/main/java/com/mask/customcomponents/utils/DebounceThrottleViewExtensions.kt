package com.mask.customcomponents.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView

/**
 * 防抖、节流 扩展函数 View
 *
 * Create by lishilin on 2025-09-15
 */

private enum class DebounceThrottleType {
    DEBOUNCE, THROTTLE
}

/************************************************************ S View.OnClickListener ************************************************************/

private class OnDebounceThrottleClickListener(
    private val type: DebounceThrottleType,
    private val timeMillis: Long,
    private val listener: View.OnClickListener
) : View.OnClickListener {

    companion object {
        fun setOnDebounceThrottleClickListener(
            type: DebounceThrottleType,
            view: View,
            timeMillis: Long,
            listener: View.OnClickListener?
        ) {
            if (listener == null) {
                view.setOnClickListener(listener)
                return
            }
            view.setOnClickListener(OnDebounceThrottleClickListener(type, timeMillis, listener))
        }
    }

    override fun onClick(view: View?) {
        if (view == null) {
            return
        }
        when (type) {
            DebounceThrottleType.DEBOUNCE -> {
                onDebounceClick(view)
            }

            DebounceThrottleType.THROTTLE -> {
                onThrottleClick(view)
            }
        }
    }

    private fun onDebounceClick(view: View) {
        DebounceThrottleUtils.debounce(view, timeMillis) {
            listener.onClick(view)
        }
    }

    private fun onThrottleClick(view: View) {
        DebounceThrottleUtils.throttle(view, timeMillis) {
            listener.onClick(view)
        }
    }
}

fun View.setOnDebounceClickListener(
    delayMillis: Long = DebounceThrottleUtils.DEFAULT_TIME_DELAY,
    listener: View.OnClickListener?
) {
    setOnDebounceClickListener(listener, delayMillis)
}

fun View.setOnDebounceClickListener(
    listener: View.OnClickListener?,
    delayMillis: Long = DebounceThrottleUtils.DEFAULT_TIME_DELAY
) {
    OnDebounceThrottleClickListener.setOnDebounceThrottleClickListener(
        DebounceThrottleType.DEBOUNCE,
        this,
        delayMillis,
        listener
    )
}

fun View.setOnThrottleClickListener(
    intervalMillis: Long = DebounceThrottleUtils.DEFAULT_TIME_INTERVAL,
    listener: View.OnClickListener?
) {
    setOnThrottleClickListener(listener, intervalMillis)
}

fun View.setOnThrottleClickListener(
    listener: View.OnClickListener?,
    intervalMillis: Long = DebounceThrottleUtils.DEFAULT_TIME_INTERVAL
) {
    OnDebounceThrottleClickListener.setOnDebounceThrottleClickListener(
        DebounceThrottleType.THROTTLE,
        this,
        intervalMillis,
        listener
    )
}

/************************************************************ E View.OnClickListener ************************************************************/

/************************************************************ S TextWatcher ************************************************************/

fun TextView.addDebounceTextChangedListener(
    textWatcher: TextWatcher?,
    delayMillis: Long = DebounceThrottleUtils.DEFAULT_TIME_DELAY
): TextWatcher? {
    if (textWatcher == null) {
        return null
    }
    val view = this
    val textWatcherInternal = object : TextWatcher {
        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            DebounceThrottleUtils.debounce(view, delayMillis) {
                textWatcher.beforeTextChanged(text, start, count, after)
            }
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            DebounceThrottleUtils.debounce(view, delayMillis) {
                textWatcher.onTextChanged(text, start, before, count)
            }
        }

        override fun afterTextChanged(text: Editable?) {
            DebounceThrottleUtils.debounce(view, delayMillis) {
                textWatcher.afterTextChanged(text)
            }
        }
    }
    addTextChangedListener(textWatcherInternal)
    return textWatcherInternal
}

/************************************************************ E TextWatcher ************************************************************/