package com.mask.customcomponents.view.index.utils

import com.github.promeg.pinyinhelper.Pinyin
import com.mask.customcomponents.view.index.interfaces.IIndexBarVo

/**
 * 字母索引栏(仿微信通讯录) 数据帮助类
 *
 * Create by lishilin on 2025-05-20
 */
object AlphabetIndexBarDataHelper {

    private val alphabetRegex = "[A-Z]".toRegex()

    fun convert(dataList: MutableList<out IIndexBarVo>) {
        dataList.forEach { data ->
            if (data.isSortConvert()) {
                // 生成用来排序的值
                val sortText = StringBuilder()
                val sortConvertTarget = data.getSortConvertTarget()
                sortConvertTarget.forEach { char ->
                    val pinyin = Pinyin.toPinyin(char).uppercase()
                    if (pinyin.isNotEmpty()) {
                        sortText.append(pinyin.substring(0, 1))
                    }
                }
                data.sortText = sortText.toString()

                // 索引栏 Tag
                if (sortText.isEmpty()) {
                    data.indexTag = IIndexBarVo.INDEX_TAG_OTHER
                } else {
                    val indexTag = sortText.substring(0, 1)
                    if (indexTag.matches(alphabetRegex)) {
                        data.indexTag = indexTag
                    } else {
                        data.indexTag = IIndexBarVo.INDEX_TAG_OTHER
                    }
                }
            }
        }
    }

    fun sort(dataList: MutableList<out IIndexBarVo>) {
        dataList.sortWith(Comparator { leftData, rightData ->
            val leftIndexTag = leftData.indexTag
            val leftSortText = leftData.sortText
            val isLeftSortConvert = leftData.isSortConvert()

            val rightIndexTag = rightData.indexTag
            val rightSortText = rightData.sortText
            val isRightSortConvert = rightData.isSortConvert()

            if (leftSortText == null && rightSortText == null) {
                0
            } else if (leftSortText == null) {
                -1
            } else if (rightSortText == null) {
                1
            } else {
                if (!isLeftSortConvert || !isRightSortConvert) {
                    0
                } else if (leftIndexTag == IIndexBarVo.INDEX_TAG_OTHER && rightIndexTag != IIndexBarVo.INDEX_TAG_OTHER) {
                    1
                } else if (leftIndexTag != IIndexBarVo.INDEX_TAG_OTHER && rightIndexTag == IIndexBarVo.INDEX_TAG_OTHER) {
                    -1
                } else {
                    leftSortText.compareTo(rightSortText)
                }
            }
        })
    }
}