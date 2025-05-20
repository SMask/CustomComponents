package com.mask.customcomponents.view.index.interfaces

/**
 * 字母索引栏 数据实体类接口
 *
 * Create by lishilin on 2025-05-19
 */
interface IIndexBarVo {

    companion object {
        const val INDEX_TAG_TOP = "↑" // 索引栏 Tag 返回顶部
        const val INDEX_TAG_OTHER = "#" // 索引栏 Tag 返回顶部

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

    var indexTag: String // 索引栏 Tag
    var sortText: String? // 用来排序的值

    fun isSortConvert(): Boolean // 是否排序转换（需要置顶等其他不需要排序的对象返回 false）

    fun getSortConvertTarget(): String // 转换为排序值的目标数据

}