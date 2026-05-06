package com.mask.customcomponents.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * 扩展函数 Context
 *
 * Create by lishilin on 2025-11-27
 */

/**
 * 根据 Context 获取 Activity
 */
fun Context?.getActivity(): Activity? {
    var currentContext: Context? = this
    while (true) {
        if (currentContext is Activity) {
            return currentContext
        }

        if (currentContext is ContextWrapper) {
            currentContext = currentContext.baseContext
        } else {
            return null
        }
    }
}

/**
 * 根据 Context 获取可用的 Activity
 *
 * 不保证对用户可见。
 */
fun Context?.getAvailableActivity(): Activity? {
    val activity = getActivity()
    return if (activity.isAvailable()) {
        activity
    } else {
        null
    }
}