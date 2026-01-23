package com.mask.customcomponents.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView LinearLayoutManager 列表分隔线
 *
 * Created by lishilin on 2016/11/15
 */
class DividerItemDecoration private constructor(
    divider: Drawable?,
    dividerWidth: Int,
    dividerHeight: Int
) : RecyclerView.ItemDecoration() {

    private val mDivider = divider ?: mDividerDefault
    private val mDividerWidth = dividerWidth
    private val mDividerHeight = dividerHeight

    private val mIgnoredPositionList = mutableListOf<Int>() // 忽略分隔线的位置集合

    var isIncludeEdge = false // 是否包含边缘(垂直布局：是否有上下分隔线； 水平布局：是否有左右分隔线；)

    companion object {

        private val mDividerDefault: Drawable
            get() {
                return Color.TRANSPARENT.toDrawable()
            }

        fun newInstance(size: Int): DividerItemDecoration {
            return newInstance(size, mDividerDefault)
        }

        fun newInstance(size: Int, @ColorInt color: Int): DividerItemDecoration {
            return newInstance(size, color.toDrawable())
        }

        fun newInstance(size: Int, divider: Drawable?): DividerItemDecoration {
            return DividerItemDecoration(divider, size, size)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (getOrientation(parent)) {
            OrientationHelper.VERTICAL -> {
                getItemOffsetsByVertical(outRect, view, parent)
            }

            OrientationHelper.HORIZONTAL -> {
                getItemOffsetsByHorizontal(outRect, view, parent)
            }
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mDivider.alpha == 0) {
            return
        }
        when (getOrientation(parent)) {
            OrientationHelper.VERTICAL -> {
                drawDividerByVertical(canvas, parent)
            }

            OrientationHelper.HORIZONTAL -> {
                drawDividerByHorizontal(canvas, parent)
            }
        }
    }

    /**
     * 获取 偏移量(垂直布局)
     */
    private fun getItemOffsetsByVertical(outRect: Rect, view: View, parent: RecyclerView) {
        // 分隔线偏移量 上
        if (isRequiredDrawDividerLeftOrTop(parent, view)) {
            outRect.top = mDividerHeight
        }

        // 分隔线偏移量 下
        if (isRequiredDrawDividerRightOrBottom(parent, view)) {
            outRect.bottom = mDividerHeight
        }
    }

    /**
     * 获取 偏移量(水平布局)
     */
    private fun getItemOffsetsByHorizontal(outRect: Rect, view: View, parent: RecyclerView) {
        // 分隔线偏移量 左
        if (isRequiredDrawDividerLeftOrTop(parent, view)) {
            outRect.left = mDividerWidth
        }

        // 分隔线偏移量 右
        if (isRequiredDrawDividerRightOrBottom(parent, view)) {
            outRect.right = mDividerWidth
        }
    }

    /**
     * 绘制 分隔线(垂直布局)
     */
    private fun drawDividerByVertical(canvas: Canvas, parent: RecyclerView) {
        val left = 0
        val right = parent.width
        parent.forEach { view ->
            // 绘制上分隔线
            if (isRequiredDrawDividerLeftOrTop(parent, view)) {
                val top = 0
                val bottom = top + mDividerHeight

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }

            // 绘制下分隔线
            if (isRequiredDrawDividerRightOrBottom(parent, view)) {
                val top = view.bottom
                val bottom = top + mDividerHeight

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }
        }
    }

    /**
     * 绘制 分隔线(水平布局)
     */
    private fun drawDividerByHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = 0
        val bottom = parent.height
        parent.forEach { view ->
            // 绘制左分隔线
            if (isRequiredDrawDividerLeftOrTop(parent, view)) {
                val left = view.left - mDividerWidth
                val right = left + mDividerWidth

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }

            // 绘制右分隔线
            if (isRequiredDrawDividerRightOrBottom(parent, view)) {
                val left = view.right
                val right = left + mDividerWidth

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }
        }
    }

    /**
     * 获取 方向
     */
    private fun getOrientation(parent: RecyclerView): Int {
        return when (val layoutManager = parent.layoutManager) {
            is LinearLayoutManager -> {
                layoutManager.orientation
            }

            else -> {
                -1
            }
        }
    }

    /**
     * 获取 位置
     */
    private fun getViewPosition(parent: RecyclerView, view: View): Int {
        return parent.getChildLayoutPosition(view)
    }

    /**
     * 是否是 第一个
     */
    private fun isViewFirst(parent: RecyclerView, view: View): Boolean {
        val position = getViewPosition(parent, view)
        return position == 0
    }

    /**
     * 是否是 最后一个
     */
    private fun isViewLast(parent: RecyclerView, view: View): Boolean {
        val position = getViewPosition(parent, view)
        val childCount = parent.adapter?.itemCount ?: 0 // item总数
        return position == childCount - 1
    }

    /**
     * 是否是 忽略的位置
     */
    private fun isViewIgnored(parent: RecyclerView, view: View): Boolean {
        val position = getViewPosition(parent, view)
        return mIgnoredPositionList.contains(position)
    }

    /**
     * 是否需要 绘制分隔线(左/上)
     *
     * 是第一排 且 包含边缘
     */
    private fun isRequiredDrawDividerLeftOrTop(parent: RecyclerView, view: View): Boolean {
        val isIncludeEdge = isIncludeEdge
        val isViewFirst = isViewFirst(parent, view)
        return isIncludeEdge && isViewFirst
    }

    /**
     * 是否需要 绘制分隔线(右/下)
     *
     * (不是最后一排 或 包含边缘) 且 没有忽略
     */
    private fun isRequiredDrawDividerRightOrBottom(parent: RecyclerView, view: View): Boolean {
        val isIncludeEdge = isIncludeEdge
        val isViewLast = isViewLast(parent, view)
        val isViewIgnored = isViewIgnored(parent, view)
        return (isIncludeEdge || !isViewLast) && !isViewIgnored
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun clearIgnoredPositionList() {
        mIgnoredPositionList.clear()
    }

    fun setIgnoredPositionList(ignoredPositionList: List<Int>?) {
        clearIgnoredPositionList()
        if (!ignoredPositionList.isNullOrEmpty()) {
            mIgnoredPositionList.addAll(ignoredPositionList)
        }
    }

    /************************************************************ E 外部调用 ************************************************************/
}