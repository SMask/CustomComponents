package com.mask.customcomponents.utils

import android.app.Activity
import android.content.Context
import com.zhuanzhuan.heroclub.common.utils.getActivity
import com.zhuanzhuan.heroclub.common.utils.isAvailable

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
     * Activity 是否可用
     */
    fun isActivityAvailable(activity: Activity?): Boolean {
        return activity.isAvailable()
    }
}