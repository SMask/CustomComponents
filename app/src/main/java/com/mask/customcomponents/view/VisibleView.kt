package com.mask.customcomponents.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.utils.CommonUtils
import com.mask.customcomponents.utils.LogUtil

/**
 * 可见性 View
 *
 * Create by lishilin on 2025-08-20
 */
class VisibleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val name by lazy {
        getTag(Global.Key.KEY_NAME.hashCode()) as? String ?: "VisibleView"
    }

    // 是否对用户可见
    private var isVisibleToUser = false

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        printLog("onAttachedToWindow")
        if (isShown) {
            dispatchOnVisibleToUser(true)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        printLog("onDetachedFromWindow")
        dispatchOnVisibleToUser(false)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        printLog("onVisibilityChanged", appendAction = { content ->
            appendLog(
                content,
                "ChangedViewVisibility",
                CommonUtils.getVisibilityText(visibility),
                9
            )
            appendLog(content, "ChangedView", changedView.javaClass.simpleName, 5)
        })
        if (isShown) {
            dispatchOnVisibleToUser(true)
        } else {
            dispatchOnVisibleToUser(false)
        }
    }

    private fun dispatchOnVisibleToUser(isVisibleToUser: Boolean) {
        if (this.isVisibleToUser == isVisibleToUser) {
            return
        }
        this.isVisibleToUser = isVisibleToUser

        val content = StringBuilder()
        content.append("dispatchOnVisibleToUser caller").append(": ")
        val callerMethodName = CommonUtils.getCallerMethodName(1)
        content.append(callerMethodName)
        LogUtil.i(content.toString())

        printLog("dispatchOnVisibleToUser", isVisibleToUser)
    }

    private fun printLog(
        key: String,
        value: Any = "",
        appendAction: ((StringBuilder) -> Unit)? = null
    ) {
        val content = StringBuilder()
        content.append(key.padEnd(20)).append(": ").append(value.toString().padEnd(5))
        content.append(" - ")
        content.append(name.padEnd(14))
        content.append(" - ")
        appendLog(content, "visibility", CommonUtils.getVisibilityText(visibility), 9)
        appendLog(content, "isShown", isShown, 5)
        appendAction?.invoke(content)
        content.append("$this")
        LogUtil.i(content.toString())
    }

    private fun appendLog(content: StringBuilder, key: String, value: Any?, valueLength: Int) {
        content.append(key).append(": ").append(value.toString().padEnd(valueLength)).append(" ; ")
    }

}