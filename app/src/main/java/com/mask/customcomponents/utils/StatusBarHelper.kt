package com.mask.customcomponents.utils

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import com.mask.customcomponents.App

/**
 * StatusBarHelper
 *
 * Create by lishilin on 2025-06-09
 */
object StatusBarHelper {

    val statusBarHeight by lazy {
        // 获取 status_bar_height 资源的ID
        val resources = App.context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            // 根据资源ID获取响应的尺寸值
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 设置 状态栏穿透
     */
    fun setStatusBarTranslucent(window: Window): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        return true
    }

    /**
     * 设置 状态栏字体图标颜色
     */
    fun setStatusBarDarkMode(window: Window, isDarkMode: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        try {
            val decorView = window.decorView
            var systemUiVisibility = decorView.systemUiVisibility
            systemUiVisibility = if (isDarkMode) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = systemUiVisibility
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun setPaddingTop(view: View) {
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + statusBarHeight,
            view.paddingRight,
            view.paddingBottom
        )
    }

    fun setMarginTop(view: View) {
        val layoutParams = view.layoutParams as? MarginLayoutParams ?: return
        layoutParams.topMargin += statusBarHeight
        view.layoutParams = layoutParams
    }

}