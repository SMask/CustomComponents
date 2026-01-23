package com.mask.customcomponents.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView

/**
 * 显示尺寸 View
 *
 * Create by lishilin on 2025-08-20
 */
class SizeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mPosition = 0

    init {
        gravity = Gravity.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        refreshText()
    }

    fun setPosition(position: Int) {
        mPosition = position
        refreshText()
    }

    fun refreshText() {
        text = "$mPosition\nwidth:$width\nheight:$height"
    }

}