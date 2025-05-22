package com.mask.customcomponents.view.index.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.R
import com.mask.customcomponents.view.index.interfaces.IHoverVo

/**
 * 列表底部总数
 *
 * Create by lishilin on 2025-05-22
 */
class TotalFooterDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val footerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44f, context.resources.displayMetrics).toInt() // 列表悬浮栏高度
    private val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, context.resources.displayMetrics) // 文字大小
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
        textPaint.textAlign = Paint.Align.CENTER

        bgPaint.isAntiAlias = true
        bgPaint.color = bgColor
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemCount = parent.adapter?.itemCount ?: return
        val position = parent.getChildLayoutPosition(view)
        if (position < itemCount - 1) {
            return
        }

        outRect.set(0, 0, 0, footerHeight)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        val itemCount = parent.adapter?.itemCount ?: return

        parent.forEach { itemView ->
            val position = parent.getChildLayoutPosition(itemView)
            if (position == itemCount - 1) {
                val layoutParams = itemView.layoutParams as RecyclerView.LayoutParams
                val top = itemView.bottom - layoutParams.bottomMargin
                drawFooter(canvas, parent, top)
            }
        }
    }

    private fun drawFooter(canvas: Canvas, parent: RecyclerView, top: Int) {
        // 绘制背景
        val left = parent.paddingStart
        val right = parent.width - parent.paddingEnd
        val bottom = top + footerHeight
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), bgPaint)

        // 绘制文字
        val text = parent.context.getString(R.string.alphabet_index_bar_footer_text, sourceDataList?.size ?: 0)
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val textHeight = textBounds.height()
        val baseline = -textBounds.top

        val textX = parent.paddingStart + (parent.width - parent.paddingStart - parent.paddingEnd) / 2f
        val textYOffset = top + (footerHeight - textHeight) / 2f
        val textY = textYOffset + baseline
        canvas.drawText(text, textX, textY, textPaint)
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setSourceDataList(sourceDataList: List<IHoverVo>): TotalFooterDecoration {
        this.sourceDataList = sourceDataList
        return this
    }

    /************************************************************ E 外部调用 ************************************************************/
}