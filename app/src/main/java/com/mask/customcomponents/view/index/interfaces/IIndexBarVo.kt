package com.mask.customcomponents.view.index.interfaces

/**
 * 字母索引栏 数据实体类接口
 *
 * Create by lishilin on 2025-05-19
 */
interface IIndexBarVo {

    companion object {
        const val INDEX_TAG_TOP = "↑" // 索引栏 Tag 返回顶部
        const val INDEX_TAG_OTHER = "#" // 索引栏 Tag 其他

        // 索引栏 Tag 数组
        val INDEX_TAG_ARR = arrayOf(
            INDEX_TAG_TOP,
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z",
            INDEX_TAG_OTHER
        )
    }

    var indexTag: String // 索引栏 Tag（主要用于索引栏定位使用，特殊值也会参与排序逻辑）

    var sortText: String? // 排序文本（主要用于列表排序使用，为 null 则在列表顶部）

    fun isSortConvert(): Boolean // 是否参与排序（false 则不会生成 indexTag 及 sortText）

    fun getSortConvertTarget(): String // 转换为排序文本的目标数据

}