package com.mask.customcomponents.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * ActivityUtils
 *
 * Create by lishilin on 2025-07-10
 */
object ActivityUtils {

    /**
     * 根据 Context 获取 Activity
     */
    fun getActivity(context: Context): Activity? {
        var currentContext: Context = context
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
}