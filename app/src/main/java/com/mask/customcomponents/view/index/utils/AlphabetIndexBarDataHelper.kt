package com.mask.customcomponents.view.index.utils

import com.github.promeg.pinyinhelper.Pinyin
import com.mask.customcomponents.view.index.enums.IndexTag
import com.mask.customcomponents.view.index.interfaces.IIndexBarVo

/**
 * 字母索引栏(仿微信通讯录) 数据帮助类
 *
 * Create by lishilin on 2025-05-20
 */
object AlphabetIndexBarDataHelper {

    fun convert(dataList: MutableList<out IIndexBarVo>) {
        dataList.forEach { data ->
            // 排序文本
            if (data.isSortConvert()) {
                val sortText = StringBuilder()
                val sortConvertTarget = data.getSortConvertTarget()
                sortConvertTarget.forEach { char ->
                    val pinyin = Pinyin.toPinyin(char).uppercase()
                    if (pinyin.isNotEmpty()) {
                        sortText.append(pinyin)
                    }
                }
                data.sortText = sortText.toString()
            }

            // 索引 Tag
            if (data.isIndexTagConvert()) {
                val sortText = data.sortText
                if (sortText.isNullOrEmpty()) {
                    data.indexTag = IndexTag.OTHER
                } else {
                    val indexTag = sortText.substring(0, 1)
                    data.indexTag = IndexTag.getInstance(indexTag)
                }
            }
        }
    }

    fun sort(dataList: MutableList<out IIndexBarVo>) {
        dataList.sortWith { leftData, rightData ->
            val leftSortText = leftData.sortText
            val leftIndexTag = leftData.indexTag

            val rightSortText = rightData.sortText
            val rightIndexTag = rightData.indexTag

            // 先比较 sortText，为 null 的在前面
            if (leftSortText == null && rightSortText == null) {
                0
            } else if (leftSortText == null) {
                -1
            } else if (rightSortText == null) {
                1
            } else {
                // 再比较 indexTag，判断特殊值的顺序
                // 置顶
                if (leftIndexTag == IndexTag.TOP && rightIndexTag != IndexTag.TOP) {
                    -1
                } else if (leftIndexTag != IndexTag.TOP && rightIndexTag == IndexTag.TOP) {
                    1
                }
                // 星标
                else if (leftIndexTag == IndexTag.STAR && rightIndexTag != IndexTag.STAR) {
                    -1
                } else if (leftIndexTag != IndexTag.STAR && rightIndexTag == IndexTag.STAR) {
                    1
                }
                // 其他
                else if (leftIndexTag == IndexTag.OTHER && rightIndexTag != IndexTag.OTHER) {
                    1
                } else if (leftIndexTag != IndexTag.OTHER && rightIndexTag == IndexTag.OTHER) {
                    -1
                }
                // 最后比较 sortText，按内容排序（此时其他排序相关字段肯定相同）
                else {
                    val result = leftSortText.compareTo(rightSortText)
                    // 如果 sortText 相同，则比较额外数据
                    if (result == 0) {
                        leftData.compareToExtra(rightData)
                    } else {
                        result
                    }
                }
            }
        }
    }
}