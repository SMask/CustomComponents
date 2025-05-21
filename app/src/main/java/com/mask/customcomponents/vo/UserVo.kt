package com.mask.customcomponents.vo

import com.mask.customcomponents.view.index.interfaces.IHoverVo
import com.mask.customcomponents.view.index.interfaces.IIndexBarVo
import com.mask.customcomponents.view.index.interfaces.IIndexBarVo.Companion.INDEX_TAG_OTHER
import com.mask.customcomponents.view.index.interfaces.IIndexBarVo.Companion.INDEX_TAG_TOP

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

    override var indexTag: String = if (isTop) INDEX_TAG_TOP else INDEX_TAG_OTHER
    override var sortText: String? = null

    override fun isShowHover(): Boolean {
        return !isTop
    }

    override fun getHoverText(): String {
        return indexTag
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
