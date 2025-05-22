package com.mask.customcomponents.view.index.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.view.index.interfaces.IHoverVo

/**
 * 列表悬浮栏
 *
 * Create by lishilin on 2025-05-20
 */
class HoverDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val hoverHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, context.resources.displayMetrics).toInt() // 列表悬浮栏高度
    private val hoverPaddingStart = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics).toInt() // 列表悬浮栏内间距
    private val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, context.resources.displayMetrics) // 文字大小
    private val textColor = Color.GRAY // 文字颜色
    private val bgColor = 0xFFF2F3F5.toInt() // 背景色

    private val textBounds = Rect()

    private val textPaint = Paint()
    private val bgPaint = Paint()

    private var sourceDataList: List<IHoverVo>? = null // 列表实际数据

    init {
        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.color = textColor

        bgPaint.isAntiAlias = true
        bgPaint.color = bgColor
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)

        val sourceDataList = sourceDataList
        val data = sourceDataList?.getOrNull(position) ?: return
        if (!data.isShowHover()) {
            return
        }

        if (position == 0) {
            outRect.set(0, hoverHeight, 0, 0)
        } else {
            val preData = sourceDataList.getOrNull(position - 1)
            if (data.getHoverText() != preData?.getHoverText()) {
                outRect.set(0, hoverHeight, 0, 0)
            }
        }
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        val position = (parent.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: return

        val sourceDataList = sourceDataList
        val data = sourceDataList?.getOrNull(position) ?: return
        if (!data.isShowHover()) {
            return
        }

        // 切换动画效果
        var isCanvasSave = false
        if (position < sourceDataList.size - 1) {
            val nextPosition = position + 1
            val nextData = sourceDataList.getOrNull(nextPosition)
            // 当第一个可见的 item 和下一个 item 的悬浮栏文字不一样时，表示即将切换
            if (data.getHoverText() != nextData?.getHoverText()) {
                // 当下一个可见 item 的 top 小于悬浮栏高度时，表示需要位移，模拟切换动画效果
                // 主要是为了兼容列表添加了其他 ItemDecoration 或者 item 布局设置了 topMargin 等等，会导致 item 之间存在间距的情况
                val nextItemView = parent.findViewHolderForLayoutPosition(nextPosition)?.itemView ?: return
                val nextLayoutParams = nextItemView.layoutParams as RecyclerView.LayoutParams
                if (nextItemView.top - nextLayoutParams.topMargin < hoverHeight * 2) {
                    canvas.save()
                    isCanvasSave = true

                    val offsetY = nextItemView.top - nextLayoutParams.topMargin - hoverHeight * 2
                    canvas.translate(0f, offsetY.toFloat())
                }
            }
        }

        drawHover(canvas, parent, parent.paddingTop, data)

        if (isCanvasSave) {
            canvas.restore()
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)

        parent.forEach { itemView ->
            val position = parent.getChildLayoutPosition(itemView)

            val sourceDataList = sourceDataList
            val data = sourceDataList?.getOrNull(position)
            if (data?.isShowHover() == true) {
                val layoutParams = itemView.layoutParams as RecyclerView.LayoutParams
                val top = itemView.top - layoutParams.topMargin - hoverHeight
                if (position == 0) {
                    drawHover(canvas, parent, top, data)
                } else {
                    val preData = sourceDataList.getOrNull(position - 1)
                    if (data.getHoverText() != preData?.getHoverText()) {
                        drawHover(canvas, parent, top, data)
                    }
                }
            }
        }
    }

    private fun drawHover(canvas: Canvas, parent: RecyclerView, top: Int, data: IHoverVo) {
        // 绘制背景
        val left = parent.paddingStart
        val right = parent.width - parent.paddingEnd
        val bottom = top + hoverHeight
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), bgPaint)

        // 绘制文字
        val text = data.getHoverText()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val textHeight = textBounds.height()
        val baseline = -textBounds.top

        val textX = parent.paddingStart + hoverPaddingStart
        val textYOffset = top + (hoverHeight - textHeight) / 2f
        val textY = textYOffset + baseline
        canvas.drawText(text, textX.toFloat(), textY, textPaint)
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setSourceDataList(sourceDataList: List<IHoverVo>): HoverDecoration {
        this.sourceDataList = sourceDataList
        return this
    }

    /************************************************************ E 外部调用 ************************************************************/
}