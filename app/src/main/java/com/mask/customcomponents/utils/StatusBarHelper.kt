package com.mask.customcomponents.utils

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
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