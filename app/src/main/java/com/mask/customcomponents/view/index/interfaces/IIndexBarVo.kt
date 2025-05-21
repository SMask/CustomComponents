package com.mask.customcomponents.view.index.interfaces

import com.mask.customcomponents.view.index.enums.IndexTag

/**
 * 字母索引栏 数据实体类接口
 *
 * Create by lishilin on 2025-05-19
 */
interface IIndexBarVo {

    var indexTag: IndexTag // 索引 Tag（主要用于索引栏定位使用，特殊值也会参与排序逻辑）

    var sortText: String? // 排序文本（主要用于列表排序使用，为 null 则在列表顶部）

    fun isSortConvert(): Boolean // 是否参与排序（false 则不会生成 indexTag 及 sortText）

    fun getSortConvertTarget(): String // 转换为排序文本的目标数据

    fun compareToExtra(other: IIndexBarVo): Int // 额外排序逻辑（主要用于 sortText 相同时的排序逻辑）
}