package com.mask.customcomponents.utils

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import java.util.concurrent.ConcurrentHashMap

/**
 * 防抖、节流 工具类
 *
 * Create by lishilin on 2025-09-04
 */
object DebounceThrottleUtils {

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val debounceMap by lazy {
        ConcurrentHashMap<String, Runnable>()
    }
    private val throttleMap by lazy {
        ConcurrentHashMap<String, Long>()
    }

    const val DEFAULT_TIME_DELAY = 500L
    const val DEFAULT_TIME_INTERVAL = 500L

    private fun getKey(view: View): String {
        return view.id.toString()
    }

    fun debounce(view: View, delayMillis: Long = DEFAULT_TIME_DELAY, action: () -> Unit) {
        debounce(getKey(view), delayMillis, action)
    }

    /**
     * 防抖
     *
     * 无论用户触发多少次事件，回调函数只会在事件停止触发指定时间间隔后执行。（即：回调函数在事件停止触发指定时间后被调用）
     *
     * @param key 唯一标识符（建议使用 ViewID）
     * @param delayMillis 时间间隔（毫秒）
     * @param action 要执行的操作
     */
    fun debounce(key: String, delayMillis: Long = DEFAULT_TIME_DELAY, action: () -> Unit) {
        // 移除之前的任务
        debounceMap[key]?.let {
            handler.removeCallbacks(it)
            debounceMap.remove(key)
        }
        // 创建新任务
        val runnable = Runnable {
            action()
            debounceMap.remove(key)
        }
        debounceMap[key] = runnable
        // 延迟执行任务
        handler.postDelayed(runnable, delayMillis)
    }

    fun throttle(view: View, intervalMillis: Long = DEFAULT_TIME_INTERVAL, action: () -> Unit) {
        throttle(getKey(view), intervalMillis, action)
    }

    /**
     * 节流
     *
     * 无论用户触发多少次事件，回调函数第一次会立即执行，在指定时间间隔后才会再次执行。（即：回调函数在规定时间内最多执行一次）
     *
     * @param key 唯一标识符（建议使用 ViewID）
     * @param intervalMillis 时间间隔（毫秒）
     * @param action 要执行的操作
     */
    fun throttle(key: String, intervalMillis: Long = DEFAULT_TIME_INTERVAL, action: () -> Unit) {
        val now = System.currentTimeMillis()
        val lastTime = throttleMap[key] ?: 0
        if (now - lastTime >= intervalMillis) {
            throttleMap[key] = now
            action()
        }
    }

    fun clear(view: View) {
        clear(getKey(view))
    }

    /**
     * 清理指定（在onDestroy中调用）
     *
     * 主要避免防抖导致的内存泄漏，节流不涉及
     */
    fun clear(key: String) {
        debounceMap[key]?.let {
            handler.removeCallbacks(it)
            debounceMap.remove(key)
        }
        throttleMap.remove(key)
    }

    /**
     * 清理全部（在onDestroy中调用）
     *
     * 主要避免防抖导致的内存泄漏，节流不涉及
     */
    fun clear() {
        handler.removeCallbacksAndMessages(null)
        debounceMap.clear()
        throttleMap.clear()
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
    if (listener == null) {
        setOnClickListener(listener)
        return
    }
    setOnClickListener { view ->
        DebounceThrottleUtils.debounce(view, delayMillis) {
            listener.onClick(view)
        }
    }
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
    if (listener == null) {
        setOnClickListener(listener)
        return
    }
    setOnClickListener { view ->
        DebounceThrottleUtils.throttle(view, intervalMillis) {
            listener.onClick(view)
        }
    }
}

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