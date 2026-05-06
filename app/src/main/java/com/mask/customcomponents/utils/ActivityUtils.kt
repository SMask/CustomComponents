package com.mask.customcomponents.utils

import android.app.Activity
import android.content.Context

/**
 * ActivityUtils
 *
 * Create by lishilin on 2025-07-10
 */
object ActivityUtils {

    /**
     * 根据 Context 获取 Activity
     */
    fun getActivity(context: Context?): Activity? {
        return context.getActivity()
    }

    /**
     * 根据 Context 获取可用的 Activity
     *
     * 不保证对用户可见。
     */
    fun getAvailableActivity(context: Context?): Activity? {
        return context.getAvailableActivity()
    }

    /**
     * Activity 是否可用
     *
     * 不保证对用户可见。
     */
    fun isActivityAvailable(activity: Activity?): Boolean {
        return activity.isAvailable()
    }
}