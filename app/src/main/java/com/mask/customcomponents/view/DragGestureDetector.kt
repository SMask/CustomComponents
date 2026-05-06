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

    // 内部配置项
    private val mTouchSlop by lazy { // 拖拽阈值（系统默认触摸敏感度）
        ViewConfiguration.get(view.context).scaledTouchSlop
    }
    private val mAnimDuration = 150L // 动画时长

    // 手势操作中数据
    private var mLastRawX = 0f
    private var mLastRawY = 0f
    private var mIsDragging = false // 是否正在拖拽

    // 外部可修改配置项
    private var mBoundMarginLeft = 0 // 距离边界的间距
    private var mBoundMarginTop = 0 // 距离边界的间距
    private var mBoundMarginRight = 0 // 距离边界的间距
    private var mBoundMarginBottom = 0 // 距离边界的间距
    private var mIsOutOfBoundsEnabled = false // 是否允许超出边界
    private var mIsHorizontalSnapEnabled = true // 是否开启水平吸附
    private var mIsVerticalSnapEnabled = false // 是否开启垂直吸附
    private var mIsAnimEnabled = true // 是否开启动画

    private var mOnDragListener: OnDragListener? = null // 监听器

    private fun onTouchEvent(event: MotionEvent): Boolean {
        val rawX = event.rawX
        val rawY = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastRawX = rawX
                mLastRawY = rawY
                mIsDragging = false
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = rawX - mLastRawX
                val dy = rawY - mLastRawY

                if (!mIsDragging && ((dx * dx + dy * dy) > mTouchSlop * mTouchSlop)) {
                    mIsDragging = true
                    mOnDragListener?.onDragStart(view, rawX, rawY)
                }

                if (mIsDragging) {
                    onDragging(dx, dy)
                    mOnDragListener?.onDragging(view, rawX, rawY, dx, dy)
                    mLastRawX = rawX
                    mLastRawY = rawY
                }
            }

            MotionEvent.ACTION_UP -> {
                if (mIsDragging) {
                    onDragEnd(event)
                    mIsDragging = false
                    return true
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (mIsDragging) {
                    onDragEnd(event)
                    mIsDragging = false
                }
            }
        }
        return mIsDragging
    }

    /**
     * 拖拽中
     */
    private fun onDragging(dx: Float, dy: Float) {
        val x = view.x + dx
        val y = view.y + dy
        if (mIsOutOfBoundsEnabled) {
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
        val x = if (mIsHorizontalSnapEnabled) {
            if (location == Location.LEFT_TOP || location == Location.LEFT_BOTTOM) {
                getBoundHorizontalMin()
            } else {
                getBoundHorizontalMax()
            }
        } else {
            view.x
        }
        val y = if (mIsVerticalSnapEnabled) {
            if (location == Location.LEFT_TOP || location == Location.RIGHT_TOP) {
                getBoundVerticalMin()
            } else {
                getBoundVerticalMax()
            }
        } else {
            view.y
        }

        mOnDragListener?.onDragEnd(view, event.rawX, event.rawY)

        // 判断是否需要移动 View
        if (x == view.x && y == view.y) {
            // 不需要移动 View，直接更新 LayoutParams
            updateLayoutParams()
            return
        }
        if (mIsAnimEnabled) {
            // 执行移动动画，完毕后更新 LayoutParams
            view.animate()
                .setInterpolator(DecelerateInterpolator())
                .setDuration(mAnimDuration)
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
        return (parentView.paddingLeft + mBoundMarginLeft).toFloat()
    }

    /**
     * 获取 边界值
     */
    private fun getBoundHorizontalMax(): Float {
        val parentView = view.parent as? ViewGroup ?: return 0f
        return (parentView.width - parentView.paddingRight - mBoundMarginRight - view.width).toFloat()
    }

    /**
     * 获取 边界值
     */
    private fun getBoundVerticalMin(): Float {
        val parentView = view.parent as? ViewGroup ?: return 0f
        return (parentView.paddingTop + mBoundMarginTop).toFloat()
    }

    /**
     * 获取 边界值
     */
    private fun getBoundVerticalMax(): Float {
        val parentView = view.parent as? ViewGroup ?: return 0f
        return (parentView.height - parentView.paddingBottom - mBoundMarginBottom - view.height).toFloat()
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

        mOnDragListener?.onUpdateLayoutParamsComplete(view, generateLayoutParams())
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
        this.mBoundMarginLeft = left
        this.mBoundMarginTop = top
        this.mBoundMarginRight = right
        this.mBoundMarginBottom = bottom
        return this
    }

    fun setOutOfBoundsEnabled(isOutOfBoundsEnabled: Boolean): DragGestureDetector {
        this.mIsOutOfBoundsEnabled = isOutOfBoundsEnabled
        return this
    }

    fun setHorizontalSnapEnabled(isHorizontalSnapEnabled: Boolean): DragGestureDetector {
        this.mIsHorizontalSnapEnabled = isHorizontalSnapEnabled
        return this
    }

    fun setVerticalSnapEnabled(isVerticalSnapEnabled: Boolean): DragGestureDetector {
        this.mIsVerticalSnapEnabled = isVerticalSnapEnabled
        return this
    }

    fun setAnimEnabled(isAnimEnabled: Boolean): DragGestureDetector {
        this.mIsAnimEnabled = isAnimEnabled
        return this
    }

    fun setOnDragListener(onDragListener: OnDragListener): DragGestureDetector {
        this.mOnDragListener = onDragListener
        return this
    }

    /************************************************************ E 外部调用 ************************************************************/
}