package com.mask.customcomponents.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.forEach
import androidx.core.view.size
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView GridLayoutManager 网格分隔线
 *
 * Created by lishilin on 2016/11/15
 */
class DividerGridItemDecoration private constructor(
    divider: Drawable?,
    dividerWidth: Int,
    dividerHeight: Int
) : RecyclerView.ItemDecoration() {

    private val mDivider = divider ?: mDividerDefault
    private val mDividerWidth = dividerWidth
    private val mDividerHeight = dividerHeight

    var isIncludeEdge = false // 是否包含边缘(垂直布局：是否有上下分隔线； 水平布局：是否有左右分隔线；)

    companion object {

        private val mDividerDefault: Drawable
            get() {
                return Color.TRANSPARENT.toDrawable()
            }

        fun newInstance(width: Int, height: Int): DividerGridItemDecoration {
            return newInstance(width, height, mDividerDefault)
        }

        fun newInstance(width: Int, height: Int, @ColorInt color: Int): DividerGridItemDecoration {
            return newInstance(width, height, color.toDrawable())
        }

        fun newInstance(width: Int, height: Int, divider: Drawable?): DividerGridItemDecoration {
            return DividerGridItemDecoration(divider, width, height)
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
                drawHorizontalDividerByVertical(canvas, parent)
                drawVerticalDividerByVertical(canvas, parent)
            }

            OrientationHelper.HORIZONTAL -> {
                drawVerticalDividerByHorizontal(canvas, parent)
                drawHorizontalDividerByHorizontal(canvas, parent)
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

        // 分隔线偏移量 左
        outRect.left = getDividerOffsetLeftByVertical(parent, view)
        // 分隔线偏移量 右
        outRect.right = getDividerOffsetRightByVertical(parent, view)
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

        // 分隔线偏移量 上
        outRect.top = getDividerOffsetTopByHorizontal(parent, view)
        // 分隔线偏移量 下
        outRect.bottom = getDividerOffsetBottomByHorizontal(parent, view)
    }

    /**
     * 获取 分隔线偏移量 左(垂直布局)
     */
    private fun getDividerOffsetLeftByVertical(parent: RecyclerView, view: View): Int {
        val spanCount = getSpanCount(parent)
        val spanIndex = getViewSpanIndex(parent, view)
        return spanIndex * mDividerWidth / spanCount
    }

    /**
     * 获取 分隔线偏移量 右(垂直布局)
     */
    private fun getDividerOffsetRightByVertical(parent: RecyclerView, view: View): Int {
        val spanCount = getSpanCount(parent)
        val spanIndex = getViewSpanIndex(parent, view)
        return mDividerWidth - (spanIndex + 1) * mDividerWidth / spanCount
    }

    /**
     * 获取 分隔线偏移量 上(水平布局)
     */
    private fun getDividerOffsetTopByHorizontal(parent: RecyclerView, view: View): Int {
        val spanCount = getSpanCount(parent)
        val spanIndex = getViewSpanIndex(parent, view)
        return spanIndex * mDividerHeight / spanCount
    }

    /**
     * 获取 分隔线偏移量 下(水平布局)
     */
    private fun getDividerOffsetBottomByHorizontal(parent: RecyclerView, view: View): Int {
        val spanCount = getSpanCount(parent)
        val spanIndex = getViewSpanIndex(parent, view)
        return mDividerHeight - (spanIndex + 1) * mDividerHeight / spanCount
    }

    /**
     * 绘制 水平分隔线(垂直布局)
     */
    private fun drawHorizontalDividerByVertical(canvas: Canvas, parent: RecyclerView) {
        // 按行遍历绘制，直接绘制一整行
        val left = 0
        val right = parent.width
        val spanCount = getSpanCount(parent)
        for (index in 0 until parent.size step spanCount) {
            val view = parent.getChildAt(index) ?: continue

            // 绘制上分隔线
            if (isRequiredDrawDividerLeftOrTop(parent, view)) {
                val top = view.top - mDividerHeight
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
     * 绘制 垂直分隔线(垂直布局)
     */
    private fun drawVerticalDividerByVertical(canvas: Canvas, parent: RecyclerView) {
        // 遍历绘制，最后一列不绘制
        parent.forEach { view ->
            // 绘制右分隔线
            if (!isViewLastSpan(parent, view)) {
                val left = view.right
                val right = left + mDividerWidth
                val top = view.top
                val bottom = view.bottom

                mDivider.setBounds(left, top, right, bottom)
                mDivider.draw(canvas)
            }
        }
    }

    /**
     * 绘制 垂直分隔线(水平布局)
     */
    private fun drawVerticalDividerByHorizontal(canvas: Canvas, parent: RecyclerView) {
        // 按列遍历绘制，直接绘制一整列
        val top = 0
        val bottom = parent.height
        val spanCount = getSpanCount(parent)
        for (index in 0 until parent.size step spanCount) {
            val view = parent.getChildAt(index) ?: continue

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
     * 绘制 水平分隔线(水平布局)
     */
    private fun drawHorizontalDividerByHorizontal(canvas: Canvas, parent: RecyclerView) {
        // 遍历绘制，最后一行不绘制
        parent.forEach { view ->
            // 绘制下分隔线
            if (!isViewLastSpan(parent, view)) {
                val left = view.left
                val right = view.right
                val top = view.bottom
                val bottom = top + mDividerHeight

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
            is GridLayoutManager -> {
                layoutManager.orientation
            }

            else -> {
                -1
            }
        }
    }

    /**
     * 获取 列数(垂直布局)/行数(水平布局)
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        return when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> {
                layoutManager.spanCount
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
     * 获取 在当前列/行的下标
     */
    private fun getViewSpanIndex(parent: RecyclerView, view: View): Int {
        val spanCount = getSpanCount(parent)
        val position = getViewPosition(parent, view)
        return position % spanCount
    }

    /**
     * 是否是 第一个 在当前列/行
     */
    private fun isViewFirstSpan(parent: RecyclerView, view: View): Boolean {
        val spanIndex = getViewSpanIndex(parent, view)
        return spanIndex == 0
    }

    /**
     * 是否是 最后一个 在当前列/行
     */
    private fun isViewLastSpan(parent: RecyclerView, view: View): Boolean {
        val spanCount = getSpanCount(parent)
        val spanIndex = getViewSpanIndex(parent, view)
        val isViewLast = isViewLast(parent, view)
        return (spanIndex == spanCount - 1) || isViewLast
    }

    /**
     * 是否是 第一排(垂直布局：第一横排； 水平布局：第一竖排；)
     */
    private fun isViewFirstLine(parent: RecyclerView, view: View): Boolean {
        val spanCount = getSpanCount(parent)
        val position = getViewPosition(parent, view)
        return position < spanCount
    }

    /**
     * 是否是 最后一排(垂直布局：最后一横排； 水平布局：最后一竖排；)
     */
    private fun isViewLastLine(parent: RecyclerView, view: View): Boolean {
        val spanCount = getSpanCount(parent)
        val position = getViewPosition(parent, view)
        val childCount = parent.adapter?.itemCount ?: 0 // item总数
        val totalLine = (childCount + spanCount - 1) / spanCount // 总排数
        val currentLine = position / spanCount // 当前排数
        return currentLine == totalLine - 1
    }

    /**
     * 是否需要 绘制分隔线(左/上)
     *
     * 是第一排 且 包含边缘
     */
    private fun isRequiredDrawDividerLeftOrTop(parent: RecyclerView, view: View): Boolean {
        val isIncludeEdge = isIncludeEdge
        val isViewFirstLine = isViewFirstLine(parent, view)
        return isIncludeEdge && isViewFirstLine
    }

    /**
     * 是否需要 绘制分隔线(右/下)
     *
     * 不是最后一排 或 包含边缘
     */
    private fun isRequiredDrawDividerRightOrBottom(parent: RecyclerView, view: View): Boolean {
        val isIncludeEdge = isIncludeEdge
        val isViewLastLine = isViewLastLine(parent, view)
        return isIncludeEdge || !isViewLastLine
    }
}