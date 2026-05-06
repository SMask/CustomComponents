package com.mask.customcomponents.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.view.isNotEmpty
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Layout to wrap a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2. The scrollable element needs to be the immediate and only child of this host layout.
 *
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 *
 * Create by lishilin on 2024-12-09
 */
// 源码地址：https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/main/java/androidx/viewpager2/integration/testapp/NestedScrollableHost.kt
class NestedScrollableHost @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val touchSlop by lazy { // 拖拽阈值（系统默认触摸敏感度）
        ViewConfiguration.get(context).scaledTouchSlop
    }

    private var parentViewPager2: ViewPager2? = null

    private val child: View?
        get() {
            return if (isNotEmpty()) {
                getChildAt(0)
            } else {
                null
            }
        }

    private var initialX = 0f
    private var initialY = 0f

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        parentViewPager2 = getParentViewPager2()
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        handleInterceptTouchEvent(event)
        return super.onInterceptTouchEvent(event)
    }

    private fun getParentViewPager2(): ViewPager2? {
        var parent: ViewParent? = parent
        while (parent != null) {
            if (parent is ViewPager2) {
                return parent
            }
            parent = parent.parent
        }
        return null
    }

    private fun handleInterceptTouchEvent(event: MotionEvent?) {
        if (event == null) {
            return
        }

        val parentOrientation = parentViewPager2?.orientation ?: return

        // Early return if child can't scroll in same direction as parent
        // 如果子控件在父控件可滚动方向上无法滚动，直接返回
        if (!canChildScroll(parentOrientation, -1f) && !canChildScroll(parentOrientation, 1f)) {
            return
        }

        val rawX = event.rawX
        val rawY = event.rawY

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = rawX
                initialY = rawY
                parent?.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = rawX - initialX
                val dy = rawY - initialY

                val scaledDx = dx.absoluteValue
                val scaledDy = dy.absoluteValue

                handleTouchEventMove(event, dx, dy, scaledDx, scaledDy)
            }
        }
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            LinearLayout.HORIZONTAL -> child?.canScrollHorizontally(direction) ?: false
            LinearLayout.VERTICAL -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    fun handleTouchEventMove(
        event: MotionEvent,
        dx: Float,
        dy: Float,
        scaledDx: Float,
        scaledDy: Float
    ) {
        val parentOrientation = parentViewPager2?.orientation ?: return
        val isParentHorizontal = parentOrientation == ViewPager2.ORIENTATION_HORIZONTAL

        if (scaledDx > touchSlop || scaledDy > touchSlop) {
            val isEventVertical = scaledDy > scaledDx
            if (isParentHorizontal == isEventVertical) {
                // Gesture is perpendicular, allow all parents to intercept
                // 父控件可滚动方向与手势滑动方向垂直
                // 通知父控件可以拦截，具体拦截与否，父控件自行处理
                parent?.requestDisallowInterceptTouchEvent(false)
            } else {
                // Gesture is parallel, query child if movement in that direction is possible
                // 父控件可滚动方向与手势滑动方向平行
                // 判断子控件在父控件可滚动方向上是否可以滚动
                if (canChildScroll(parentOrientation, if (isParentHorizontal) dx else dy)) {
                    // Child can scroll, disallow all parents to intercept
                    parent?.requestDisallowInterceptTouchEvent(true)
                } else {
                    // Child cannot scroll, allow all parents to intercept
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
    }
}