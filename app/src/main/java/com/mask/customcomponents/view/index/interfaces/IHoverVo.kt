package com.mask.customcomponents.view.index.interfaces

/**
 * 列表悬浮栏 数据实体类接口
 *
 * Create by lishilin on 2025-05-19
 */
interface IHoverVo {

    fun isShowHover(): Boolean // 是否显示悬浮栏

    fun getHoverText(): String // 悬浮栏显示内容

}