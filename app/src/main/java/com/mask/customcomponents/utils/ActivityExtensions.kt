package com.zhuanzhuan.heroclub.common.utils

import android.app.Activity

/**
 * 扩展函数 Activity
 *
 * Create by lishilin on 2025-11-27
 */

/**
 * Activity 是否可用
 *
 * 不保证对用户可见。
 */
fun Activity?.isAvailable(): Boolean {
    if (this == null) {
        return false
    }
    if (isFinishing || isDestroyed) {
        return false
    }
    return true
}