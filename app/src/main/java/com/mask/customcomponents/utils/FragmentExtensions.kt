package com.zhuanzhuan.heroclub.common.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.mask.customcomponents.App

/**
 * 扩展函数 Fragment
 *
 * Create by lishilin on 2025-11-25
 */

/**
 * 获取 CharSequence
 *
 * 拦截崩溃
 */
fun Fragment.getTextSafely(@StringRes resId: Int): CharSequence {
    try {
        val context = context ?: App.context
        return context.getText(resId)
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

/**
 * 获取 字符串
 *
 * 拦截崩溃
 */
fun Fragment.getStringSafely(@StringRes resId: Int): String {
    try {
        val context = context ?: App.context
        return context.getString(resId)
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

/**
 * 获取 字符串
 *
 * 拦截崩溃
 */
fun Fragment.getStringSafely(@StringRes resId: Int, vararg formatArgs: Any): String {
    try {
        val context = context ?: App.context
        return context.getString(resId, *formatArgs)
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

/**
 * 获取 颜色
 *
 * 拦截崩溃
 */
@ColorInt
fun Fragment.getColorSafely(@ColorRes resId: Int): Int {
    try {
        val context = context ?: App.context
        return context.getColor(resId)
    } catch (e: Exception) {
        e.printStackTrace()
        return Color.TRANSPARENT
    }
}

/**
 * 获取 ColorStateList
 *
 * 拦截崩溃
 */
fun Fragment.getColorStateListSafely(@ColorRes resId: Int): ColorStateList {
    try {
        val context = context ?: App.context
        return context.getColorStateList(resId)
    } catch (e: Exception) {
        e.printStackTrace()
        return ColorStateList.valueOf(Color.TRANSPARENT)
    }
}

/**
 * 获取 Drawable
 *
 * 拦截崩溃
 */
fun Fragment.getDrawableSafely(@DrawableRes resId: Int): Drawable? {
    try {
        val context = context ?: App.context
        return AppCompatResources.getDrawable(context, resId)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

/**
 * Fragment 是否可用
 *
 * 不保证对用户可见，是否对用户可见可使用 BaseVisibleCallbackFragment.isVisibleToUser 字段判断。
 */
fun Fragment?.isAvailable(): Boolean {
    if (this == null) {
        return false
    }
    // 判断 Activity 容器
    if (context == null) {
        return false
    }
    if (!activity.isAvailable()) {
        return false
    }
    // 判断 Fragment 本身
    if (view == null) {
        return false
    }
    if (!isAdded) {
        return false
    }
    if (isRemoving) {
        return false
    }
    if (isDetached) {
        return false
    }
    return true
}