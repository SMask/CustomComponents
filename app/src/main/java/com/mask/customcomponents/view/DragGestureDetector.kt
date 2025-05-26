package com.mask.customcomponents.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

/**
 * 拖拽 GestureDetector
 *
 * Create by lishilin on 2025-05-23
 */
class DragGestureDetector(val view: View) {

    /**
     * 监听器
     */
    abstract class OnDragListener {

        open fun onDragStart(view: View, rawX: Float, rawY: Float) {}

        open fun onDragging(view: View, rawX: Float, rawY: Float, dx: Float, dy: Float) {}

        open fun onDragEnd(view: View, rawX: Float, rawY: Float) {}

        open fun onUpdateLayoutParamsComplete(view: View, layoutParams: ViewGroup.LayoutParams) {}
    }

    /**
     * 位置
     */
    private enum class Location {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    // 手势操作中数据
    private var lastRawX = 0f
    private var lastRawY = 0f
    private var isDragging = false // 是否正在拖拽

    // 内部配置项
    private val touchSlop by lazy { // 拖拽阈值（系统默认触摸敏感度）
        ViewConfiguration.get(view.context).scaledTouchSlop
    }
    private val animDuration = 150L // 动画时长

    // 外部可修改配置项
    private var boundMarginLeft = 0 // 距离边界的间距
    private var boundMarginTop = 0 // 距离边界的间距
    private var boundMarginRight = 0 // 距离边界的间距
    private var boundMarginBottom = 0 // 距离边界的间距
    private var isOutOfBoundsEnabled = false // 是否允许超出边界
    private var isHorizontalSnapEnabled = true // 是否开启水平吸附
    private var isVerticalSnapEnabled = false // 是否开启垂直吸附
    private var isAnimEnabled = true // 是否开启动画

    private var onDragListener: OnDragListener? = null // 监听器

    private fun onTouchEvent(event: MotionEvent): Boolean {
        val rawX = event.rawX
        val rawY = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastRawX = rawX
                lastRawY = rawY
                isDragging = false
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = rawX - lastRawX
                val dy = rawY - lastRawY

                if (!isDragging && ((dx * dx + dy * dy) > touchSlop * touchSlop)) {
                    isDragging = true
                    onDragListener?.onDragStart(view, rawX, rawY)
                }

                if (isDragging) {
                    onDragging(dx, dy)
                    onDragListener?.onDragging(view, rawX, rawY, dx, dy)
                    lastRawX = rawX
                    lastRawY = rawY
                }
            }

            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    onDragEnd(event)
                    isDragging = false
                    return true
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (isDragging) {
                    onDragEnd(event)
                    isDragging = false
                }
            }
        }
        return isDragging
    }

    /**
     * 拖拽中
     */
    private fun onDragging(dx: Float, dy: Float) {
        val x = view.x + dx
        val y = view.y + dy
        if (isOutOfBoundsEnabled) {
            view.x = x
            view.y = y
        } else {
            view.x = x.coerceAtLeast(getBoundHorizontalMin()).coerceAtMost(getBoundHorizontalMax())
            view.y = y.coerceAtLeast(getBoundVerticalMin()).coerceAtMost(getBoundVerticalMax())
        }
    }

    /**
     * 拖拽结束
     */
    private fun onDragEnd(event: MotionEvent) {
        // 获取 View 最终的位置
        // 吸附需要贴边
        val location = getLocation()
        val x = if (isHorizontalSnapEnabled) {
            if (location == Location.LEFT_TOP || location == Location.LEFT_BOTTOM) {
                getBoundHorizontalMin()
            } else {
                getBoundHorizontalMax()
            }
        } else {
            view.x
        }
        val y = if (isVerticalSnapEnabled) {
            if (location == Location.LEFT_TOP || location == Location.RIGHT_TOP) {
                getBoundVerticalMin()
            } else {
                getBoundVerticalMax()
            }
        } else {
            view.y
        }

        onDragListener?.onDragEnd(view, event.rawX, event.rawY)

        // 判断是否需要移动 View
        if (x == view.x && y == view.y) {
            // 不需要移动 View，直接更新 LayoutParams
            updateLayoutParams()
            return
        }
        if (isAnimEnabled) {
            // 执行移动动画，完毕后更新 LayoutParams
            view.animate()
                .setInterpolator(DecelerateInterpolator())
                .setDuration(animDuration)
                .x(x)
                .y(y)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        updateLayoutParams()
                    }
                })
                .start()
        } else {
            // 直接移动 View，然后更新 LayoutParams
            view.x = x
            view.y = y
            updateLayoutParams()
        }
    }

    /**
     * 获取 边界值
     */
    private fun getBoundHorizontalMin(): Float {
        val parentView = view.parent as? ViewGroup ?: return 0f
        return (parentView.paddingLeft + boundMarginLeft).toFloat()
    }

    /**
     * 获取 边界值
     */
    private fun getBoundHorizontalMax(): Float {
        val parentView = view.parent as? ViewGroup ?: return 0f
        return (parentView.width - parentView.paddingRight - boundMarginRight - view.width).toFloat()
    }

    /**
     * 获取 边界值
     */
    private fun getBoundVerticalMin(): Float {
        val parentView = view.parent as? ViewGroup ?: return 0f
        return (parentView.paddingTop + boundMarginTop).toFloat()
    }

    /**
     * 获取 边界值
     */
    private fun getBoundVerticalMax(): Float {
        val parentView = view.parent as? ViewGroup ?: return 0f
        return (parentView.height - parentView.paddingBottom - boundMarginBottom - view.height).toFloat()
    }

    /**
     * 获取 位置
     */
    private fun getLocation(): Location {
        val parentView = view.parent as? ViewGroup ?: return Location.LEFT_TOP
        val parentCenterX = parentView.width / 2f
        val parentCenterY = parentView.height / 2f
        val viewCenterX = view.x + view.width / 2f
        val viewCenterY = view.y + view.height / 2f

        return if (viewCenterX <= parentCenterX) {
            if (viewCenterY <= parentCenterY) {
                Location.LEFT_TOP
            } else {
                Location.LEFT_BOTTOM
            }
        } else {
            if (viewCenterY <= parentCenterY) {
                Location.RIGHT_TOP
            } else {
                Location.RIGHT_BOTTOM
            }
        }
    }

    /**
     * 更新 LayoutParams，保存最终位置，确保位置在屏幕旋转或重新布局时保持不变。
     */
    private fun updateLayoutParams() {
        val layoutParams = view.layoutParams

        // 设置位置
        val isUpdateLayoutParams = when (layoutParams) {
            is FrameLayout.LayoutParams -> {
                updateLayoutParamsGravity(layoutParams)
                true
            }

            is ConstraintLayout.LayoutParams -> {
                updateLayoutParamsGravity(layoutParams)
                true
            }

            else -> {
                false
            }
        }

        // 没更新 LayoutParams，直接返回
        if (!isUpdateLayoutParams) {
            return
        }

        // 重置偏移量
        view.translationX = 0f
        view.translationY = 0f

        // 重新设置 LayoutParams，不然 ConstraintLayout 布局不会生效
        view.layoutParams = layoutParams

        onDragListener?.onUpdateLayoutParamsComplete(view, generateLayoutParams())
    }

    private fun updateLayoutParamsGravity(layoutParams: FrameLayout.LayoutParams) {
        // 重新设置位置
        when (getLocation()) {
            Location.LEFT_TOP -> {
                layoutParams.gravity = Gravity.LEFT or Gravity.TOP
            }

            Location.LEFT_BOTTOM -> {
                layoutParams.gravity = Gravity.LEFT or Gravity.BOTTOM
            }

            Location.RIGHT_TOP -> {
                layoutParams.gravity = Gravity.RIGHT or Gravity.TOP
            }

            Location.RIGHT_BOTTOM -> {
                layoutParams.gravity = Gravity.RIGHT or Gravity.BOTTOM
            }
        }

        // 设置外边距
        updateLayoutParamsMargin(layoutParams)
    }

    private fun updateLayoutParamsGravity(layoutParams: ConstraintLayout.LayoutParams) {
        // 重置布局参数，避免其他约束关系影响控件位置
        layoutParams.guideBegin = ConstraintSet.UNSET
        layoutParams.guideEnd = ConstraintSet.UNSET
        layoutParams.guidePercent = ConstraintSet.UNSET.toFloat()
        layoutParams.leftToLeft = ConstraintSet.UNSET
        layoutParams.leftToRight = ConstraintSet.UNSET
        layoutParams.rightToLeft = ConstraintSet.UNSET
        layoutParams.rightToRight = ConstraintSet.UNSET
        layoutParams.topToTop = ConstraintSet.UNSET
        layoutParams.topToBottom = ConstraintSet.UNSET
        layoutParams.bottomToTop = ConstraintSet.UNSET
        layoutParams.bottomToBottom = ConstraintSet.UNSET
        layoutParams.baselineToBaseline = ConstraintSet.UNSET
        layoutParams.baselineToTop = ConstraintSet.UNSET
        layoutParams.baselineToBottom = ConstraintSet.UNSET
        layoutParams.circleConstraint = ConstraintSet.UNSET
        layoutParams.startToEnd = ConstraintSet.UNSET
        layoutParams.startToStart = ConstraintSet.UNSET
        layoutParams.endToStart = ConstraintSet.UNSET
        layoutParams.endToEnd = ConstraintSet.UNSET
        layoutParams.horizontalWeight = ConstraintSet.UNSET.toFloat()
        layoutParams.verticalWeight = ConstraintSet.UNSET.toFloat()
        layoutParams.editorAbsoluteX = ConstraintSet.UNSET
        layoutParams.editorAbsoluteY = ConstraintSet.UNSET
        layoutParams.orientation = ConstraintSet.UNSET

        // 重新设置约束关系
        when (getLocation()) {
            Location.LEFT_TOP -> {
                layoutParams.leftToLeft = ConstraintSet.PARENT_ID
                layoutParams.topToTop = ConstraintSet.PARENT_ID
            }

            Location.LEFT_BOTTOM -> {
                layoutParams.leftToLeft = ConstraintSet.PARENT_ID
                layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
            }

            Location.RIGHT_TOP -> {
                layoutParams.rightToRight = ConstraintSet.PARENT_ID
                layoutParams.topToTop = ConstraintSet.PARENT_ID
            }

            Location.RIGHT_BOTTOM -> {
                layoutParams.rightToRight = ConstraintSet.PARENT_ID
                layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
            }
        }

        // 设置外边距
        updateLayoutParamsMargin(layoutParams)
    }

    private fun updateLayoutParamsMargin(layoutParams: ViewGroup.MarginLayoutParams) {
        val left = view.x
        val top = view.y
        val right = left + view.width
        val bottom = top + view.height

        val parentView = view.parent as? ViewGroup
        val parentWidth = parentView?.width ?: 0
        val parentHeight = parentView?.height ?: 0
        val parentPaddingLeft = parentView?.paddingLeft ?: 0
        val parentPaddingTop = parentView?.paddingTop ?: 0
        val parentPaddingRight = parentView?.paddingRight ?: 0
        val parentPaddingBottom = parentView?.paddingBottom ?: 0

        val leftMargin = (left - parentPaddingLeft).toInt()
        val topMargin = (top - parentPaddingTop).toInt()
        val rightMargin = (parentWidth - right - parentPaddingRight).toInt()
        val bottomMargin = (parentHeight - bottom - parentPaddingBottom).toInt()

        // 重置 Margin 为 0，避免异常情况出现
        layoutParams.leftMargin = 0
        layoutParams.topMargin = 0
        layoutParams.rightMargin = 0
        layoutParams.bottomMargin = 0
        // 根据位置重新设置 Margin
        when (getLocation()) {
            Location.LEFT_TOP -> {
                layoutParams.leftMargin = leftMargin
                layoutParams.topMargin = topMargin
            }

            Location.LEFT_BOTTOM -> {
                layoutParams.leftMargin = leftMargin
                layoutParams.bottomMargin = bottomMargin
            }

            Location.RIGHT_TOP -> {
                layoutParams.rightMargin = rightMargin
                layoutParams.topMargin = topMargin
            }

            Location.RIGHT_BOTTOM -> {
                layoutParams.rightMargin = rightMargin
                layoutParams.bottomMargin = bottomMargin
            }
        }
    }

    private fun generateLayoutParams(): ViewGroup.LayoutParams {
        return when (val layoutParams = view.layoutParams) {
            is FrameLayout.LayoutParams -> {
                FrameLayout.LayoutParams(layoutParams)
            }

            is ConstraintLayout.LayoutParams -> {
                ConstraintLayout.LayoutParams(layoutParams)
            }

            else -> {
                ViewGroup.LayoutParams(view.width, view.height)
            }
        }
    }

    /************************************************************ S 外部调用 ************************************************************/

    @SuppressLint("ClickableViewAccessibility")
    fun attach() {
        view.setOnTouchListener { _, event ->
            onTouchEvent(event)
        }
    }

    fun setBoundMargin(boundMargin: Int): DragGestureDetector {
        return setBoundMargin(boundMargin, boundMargin)
    }

    fun setBoundMargin(horizontal: Int, vertical: Int): DragGestureDetector {
        return setBoundMargin(horizontal, vertical, horizontal, vertical)
    }

    fun setBoundMargin(left: Int, top: Int, right: Int, bottom: Int): DragGestureDetector {
        this.boundMarginLeft = left
        this.boundMarginTop = top
        this.boundMarginRight = right
        this.boundMarginBottom = bottom
        return this
    }

    fun setOutOfBoundsEnabled(isOutOfBoundsEnabled: Boolean): DragGestureDetector {
        this.isOutOfBoundsEnabled = isOutOfBoundsEnabled
        return this
    }

    fun setHorizontalSnapEnabled(isHorizontalSnapEnabled: Boolean): DragGestureDetector {
        this.isHorizontalSnapEnabled = isHorizontalSnapEnabled
        return this
    }

    fun setVerticalSnapEnabled(isVerticalSnapEnabled: Boolean): DragGestureDetector {
        this.isVerticalSnapEnabled = isVerticalSnapEnabled
        return this
    }

    fun setAnimEnabled(isAnimEnabled: Boolean): DragGestureDetector {
        this.isAnimEnabled = isAnimEnabled
        return this
    }

    fun setOnDragListener(onDragListener: OnDragListener): DragGestureDetector {
        this.onDragListener = onDragListener
        return this
    }

    /************************************************************ E 外部调用 ************************************************************/
}