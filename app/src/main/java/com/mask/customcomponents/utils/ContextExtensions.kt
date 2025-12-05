package com.zhuanzhuan.heroclub.common.utils

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