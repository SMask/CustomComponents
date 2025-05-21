package com.mask.customcomponents.vo

import com.mask.customcomponents.view.index.enums.IndexTag
import com.mask.customcomponents.view.index.interfaces.IHoverVo
import com.mask.customcomponents.view.index.interfaces.IIndexBarVo

/**
 * 用户 实体类
 *
 * Create by lishilin on 2025-05-19
 */
data class UserVo(
    val id: String?,
    val name: String?,
    val timestamp: Long?,
    val isTop: Boolean = false,
) : IHoverVo, IIndexBarVo {

    override var indexTag: IndexTag = if (isTop) IndexTag.TOP else IndexTag.OTHER
    override var sortText: String? = null

    override fun isShowHover(): Boolean {
        return !isTop
    }

    override fun getHoverText(): String {
        return indexTag.value
    }

    override fun isSortConvert(): Boolean {
        return !isTop
    }

    override fun getSortConvertTarget(): String {
        return name ?: ""
    }

    override fun compareToExtra(other: IIndexBarVo): Int {
        val otherTimestamp = (other as? UserVo)?.timestamp

        return if (timestamp == null && otherTimestamp == null) {
            0
        } else if (timestamp == null) {
            -1
        } else if (otherTimestamp == null) {
            1
        } else {
            timestamp.compareTo(otherTimestamp)
        }
    }
}
