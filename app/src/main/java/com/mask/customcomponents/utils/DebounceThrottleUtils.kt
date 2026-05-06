package com.mask.customcomponents.utils

import android.os.Handler
import android.os.Looper
import android.view.View
import java.util.concurrent.ConcurrentHashMap

/**
 * 防抖、节流 工具类
 *
 * Create by lishilin on 2025-09-04
 */
object DebounceThrottleUtils {

    private val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val mDebounceMap by lazy {
        ConcurrentHashMap<String, Runnable>()
    }
    private val mThrottleMap by lazy {
        ConcurrentHashMap<String, Long>()
    }

    const val DEFAULT_TIME_DELAY = 500L
    const val DEFAULT_TIME_INTERVAL = 500L

    fun getKey(view: View): String {
        return "${view.id}_${view.hashCode()}"
    }

    fun debounce(view: View, delayMillis: Long = DEFAULT_TIME_DELAY, action: () -> Unit) {
        debounce(getKey(view), delayMillis, action)
    }

    /**
     * 防抖
     *
     * 无论用户触发多少次事件，回调函数只会在事件停止触发指定时间间隔后执行。（即：回调函数在事件停止触发指定时间后被调用）
     *
     * @param key 唯一标识符（建议使用 ViewID、hashCode）
     * @param delayMillis 时间间隔（毫秒）
     * @param action 要执行的操作
     */
    fun debounce(key: String, delayMillis: Long = DEFAULT_TIME_DELAY, action: () -> Unit) {
        // 移除之前的任务
        mDebounceMap[key]?.let {
            mHandler.removeCallbacks(it)
            mDebounceMap.remove(key)
        }
        // 创建新任务
        val runnable = Runnable {
            action()
            mDebounceMap.remove(key)
        }
        mDebounceMap[key] = runnable
        // 延迟执行任务
        mHandler.postDelayed(runnable, delayMillis)
    }

    fun throttle(view: View, intervalMillis: Long = DEFAULT_TIME_INTERVAL, action: () -> Unit) {
        throttle(getKey(view), intervalMillis, action)
    }

    /**
     * 节流
     *
     * 无论用户触发多少次事件，回调函数第一次会立即执行，在指定时间间隔后才会再次执行。（即：回调函数在规定时间内最多执行一次）
     *
     * @param key 唯一标识符（建议使用 ViewID、hashCode）
     * @param intervalMillis 时间间隔（毫秒）
     * @param action 要执行的操作
     */
    fun throttle(key: String, intervalMillis: Long = DEFAULT_TIME_INTERVAL, action: () -> Unit) {
        val now = System.currentTimeMillis()
        val lastTime = mThrottleMap[key] ?: 0
        if (now - lastTime >= intervalMillis) {
            mThrottleMap[key] = now
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
        mDebounceMap[key]?.let {
            mHandler.removeCallbacks(it)
            mDebounceMap.remove(key)
        }
        mThrottleMap.remove(key)
    }

    /**
     * 清理全部（在onDestroy中调用）
     *
     * 主要避免防抖导致的内存泄漏，节流不涉及
     */
    fun clear() {
        mHandler.removeCallbacksAndMessages(null)
        mDebounceMap.clear()
        mThrottleMap.clear()
    }
}