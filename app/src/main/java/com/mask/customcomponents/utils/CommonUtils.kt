package com.mask.customcomponents.utils

import android.view.View

/**
 * 公共方法
 *
 * Create by lishilin on 2025-08-20
 */
object CommonUtils {

    fun getVisibilityText(visibility: Int?): String {
        return when (visibility) {
            View.VISIBLE -> "VISIBLE"
            View.INVISIBLE -> "INVISIBLE"
            View.GONE -> "GONE"
            else -> "UNKNOWN"
        }
    }

    /**
     * 获取调用者的方法
     * level 为 0 时，获取当前调用者的方法
     * level 为 1 时，获取调用当前方法的调用者的方法
     * 以此类推
     */
    fun getCallerMethod(level: Int = 0): StackTraceElement? {
        val stackTrace = Thread.currentThread().stackTrace ?: return null
        val index = 3 + level
        return stackTrace.getOrNull(index)
    }

    /**
     * 获取调用者的方法名
     * level 为 0 时，获取当前调用者的方法名
     * level 为 1 时，获取调用当前方法的调用者的方法名
     * 以此类推
     */
    fun getCallerMethodName(level: Int = 0): String {
        val caller = getCallerMethod(level) ?: return "UNKNOWN"
        return caller.methodName
    }
}