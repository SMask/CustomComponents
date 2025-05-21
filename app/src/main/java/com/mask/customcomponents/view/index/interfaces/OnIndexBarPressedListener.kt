package com.mask.customcomponents.view.index.interfaces

import com.mask.customcomponents.view.index.enums.IndexTag

/**
 * 字母索引栏 按下监听
 *
 * Create by lishilin on 2025-05-19
 */
interface OnIndexBarPressedListener {
    fun onIndexPressed(index: Int, tag: IndexTag)

    fun onMotionEventEnd()
}