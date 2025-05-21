package com.mask.customcomponents.view.index

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.view.index.enums.IndexTag
import com.mask.customcomponents.view.index.interfaces.IIndexBarVo
import com.mask.customcomponents.view.index.interfaces.OnIndexBarPressedListener
import com.mask.customcomponents.view.index.utils.AlphabetIndexBarDataHelper

/**
 * 字母索引栏(仿微信通讯录)
 *
 * Create by lishilin on 2025-05-19
 */
class AlphabetIndexBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var width = 0 // View的宽度
    private var height = 0 // View的高度

    private var tagHeight = 0 // 每个字母索引的高度

    private val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics) // 字母索引文字大小
    private val textColor = Color.GRAY // 字母索引文字颜色

    private val bgColorNormal = 0x00000000 // 正常时的背景色
    private val bgColorPressed = 0x40000000 // 按下时的背景色

    private val tagBounds = Rect()

    private val tagPaint = Paint()

    private var isRealIndex = false // 是否使用真实的字母索引（根据数据内容生成字母索引）

    private val tagList = mutableListOf<IndexTag>() // 字母索引数据

    private var sourceDataList: MutableList<out IIndexBarVo>? = null // 实际列表数据

    private var rvContent: RecyclerView? = null // 需要联动的列表

    private var pressDisplayTextView: TextView? = null // 按下时需要显示的 TextView

    private var onIndexBarPressedListener: OnIndexBarPressedListener? = null

    init {
        tagPaint.isAntiAlias = true
        tagPaint.textSize = textSize
        tagPaint.color = textColor
        tagPaint.textAlign = Paint.Align.CENTER

        setBackgroundColor(bgColorNormal)

        initTagList()

        setOnIndexBarPressedListener(object : OnIndexBarPressedListener {
            override fun onIndexPressed(index: Int, tag: IndexTag) {
                pressDisplayTextView?.isVisible = true
                pressDisplayTextView?.text = tag.value

                val rvContent = rvContent
                val layoutManager = rvContent?.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val position = getPositionByTag(tag)
                    if (position >= 0) {
                        rvContent.stopScroll()
                        layoutManager.scrollToPositionWithOffset(position, 0)
                    }
                }
            }

            override fun onMotionEventEnd() {
                pressDisplayTextView?.isVisible = false
            }
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        width = w
        height = h

        computeTagHeight()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var measuredWidth = 0
        var measuredHeight = 0

        tagList.forEach { tag ->
            val tagStr = tag.value
            tagPaint.getTextBounds(tagStr, 0, tagStr.length, tagBounds)
            measuredWidth = measuredWidth.coerceAtLeast(tagBounds.width())
            measuredHeight = measuredHeight.coerceAtLeast(tagBounds.height())
        }
        measuredHeight *= tagList.size

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                measuredWidth = widthSize
            }

            MeasureSpec.AT_MOST -> {
                measuredWidth = measuredWidth.coerceAtMost(widthSize)
            }

            MeasureSpec.UNSPECIFIED -> {
            }
        }

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                measuredHeight = heightSize
            }

            MeasureSpec.AT_MOST -> {
                measuredHeight = measuredHeight.coerceAtMost(heightSize)
            }

            MeasureSpec.UNSPECIFIED -> {
            }
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingTop = paddingTop

        tagList.forEachIndexed { index, tag ->
            val tagStr = tag.value
            tagPaint.getTextBounds(tagStr, 0, tagStr.length, tagBounds)
            // 居中绘制，不需要再计算文本宽度
//            val textWidth = tagPaint.measureText(tag) // 采用这种方式计算宽度，是避免文字默认间距影响左右居中效果
            val textHeight = tagBounds.height() // 采用这种方式计算高度，是希望文字上下绝对居中，不受默认间距影响
            val baseline = -tagBounds.top

            val textX = width / 2f
            val textYOffset = paddingTop + index * tagHeight + (tagHeight - textHeight) / 2f
            val textY = textYOffset + baseline
            canvas.drawText(tagStr, textX, textY, tagPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                setBackgroundColor(bgColorPressed)
                onTouchMove(event)
            }

            MotionEvent.ACTION_POINTER_DOWN -> { // 不需要处理多点触控
            }

            MotionEvent.ACTION_MOVE -> {
                onTouchMove(event)
            }

            else -> {
                setBackgroundColor(bgColorNormal)

                onIndexBarPressedListener?.onMotionEventEnd()
            }
        }

        return true
    }

    /**
     * 计算每个字母索引的高度
     * 当 View 宽高及 tagList 数据发生变化时需要调用此方法
     */
    private fun computeTagHeight() {
        tagHeight = if (tagList.isEmpty()) {
            0
        } else {
            (height - paddingTop - paddingBottom) / tagList.size
        }
    }

    private fun onTouchMove(event: MotionEvent?) {
        if (event == null) {
            return
        }

        val paddingTop = paddingTop

        val y = event.y

        val position = ((y - paddingTop) / tagHeight).toInt()
            .coerceAtLeast(0)
            .coerceAtMost(tagList.size - 1) // 不使用 coerceIn 以免 tagList.size 为 0 时，导致抛出异常崩溃

        val tag = tagList.getOrNull(position)
        if (tag != null) {
            onIndexBarPressedListener?.onIndexPressed(position, tag)
        }
    }

    private fun initTagList() {
        tagList.clear()

        if (!isRealIndex) {
            tagList.addAll(IndexTag.ARR)
        } else {
            sourceDataList?.forEach { data ->
                val tag = data.indexTag
                if (!tagList.contains(tag)) {
                    tagList.add(tag)
                }
            }
        }

        computeTagHeight()
    }

    private fun initSourceDataList() {
        val sourceDataList = sourceDataList
        if (sourceDataList.isNullOrEmpty()) {
            return
        }

        convertSourceDataList()
        sortSourceDataList()
        if (isRealIndex) {
            initTagList()
        }
    }

    private fun convertSourceDataList() {
        val sourceDataList = sourceDataList
        if (sourceDataList.isNullOrEmpty()) {
            return
        }

        AlphabetIndexBarDataHelper.convert(sourceDataList)
    }

    private fun sortSourceDataList() {
        val sourceDataList = sourceDataList
        if (sourceDataList.isNullOrEmpty()) {
            return
        }

        AlphabetIndexBarDataHelper.sort(sourceDataList)
    }

    private fun getPositionByTag(tag: IndexTag): Int {
        val sourceDataList = sourceDataList
        if (sourceDataList.isNullOrEmpty()) {
            return -1
        }
        sourceDataList.forEachIndexed { index, data ->
            if (data.indexTag == tag) {
                return index
            }
        }
        return -1
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setRealIndex(isRealIndex: Boolean): AlphabetIndexBar {
        if (this.isRealIndex == isRealIndex) {
            return this
        }
        this.isRealIndex = isRealIndex
        initTagList()
        return this
    }

    fun setSourceDataList(sourceDataList: MutableList<out IIndexBarVo>): AlphabetIndexBar {
        this.sourceDataList = sourceDataList
        initSourceDataList()
        invalidate()
        return this
    }

    fun setRecyclerView(rvContent: RecyclerView): AlphabetIndexBar {
        this.rvContent = rvContent
        return this
    }

    fun setPressDisplayTextView(pressDisplayTextView: TextView): AlphabetIndexBar {
        this.pressDisplayTextView = pressDisplayTextView
        return this
    }

    fun setOnIndexBarPressedListener(onIndexBarPressedListener: OnIndexBarPressedListener): AlphabetIndexBar {
        this.onIndexBarPressedListener = onIndexBarPressedListener
        return this
    }

    /************************************************************ E 外部调用 ************************************************************/
}