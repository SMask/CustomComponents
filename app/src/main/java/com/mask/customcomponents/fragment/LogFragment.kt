package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.View
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.utils.LogUtil

/**
 * Fragment 打印日志
 *
 * Create by lishilin on 2025-07-24
 */
abstract class LogFragment : BaseFragment() {

    protected val name by lazy {
        arguments?.getString(Global.Key.KEY_NAME) ?: "name"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        printLog("onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        printLog("onStart")
    }

    override fun onResume() {
        super.onResume()
        printLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        printLog("onPause")
    }

    override fun onStop() {
        super.onStop()
        printLog("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        printLog("onDestroyView")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        printLog("onHiddenChanged", hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        printLog("setUserVisibleHint", isVisibleToUser)
    }

    override fun onGlobalLayout() {
        super.onGlobalLayout()
        printLog("onGlobalLayout")
    }

    override fun onVisibleToUser(isVisibleToUser: Boolean) {
        super.onVisibleToUser(isVisibleToUser)

        val content = StringBuilder()
        content.append("onVisibleToUser caller").append(": ")
        val callerMethodName = getCallerMethodName(2)
        content.append(callerMethodName)
        if (callerMethodName == "dispatchChildOnVisibleToUser") {
            content.append(" - ").append(getCallerMethodName(4))
        }
        LogUtil.i(content.toString())

        printLog("onVisibleToUser", isVisibleToUser)
    }

    protected fun setName(name: String) {
        if (arguments == null) {
            arguments = Bundle()
        }
        arguments?.putString(Global.Key.KEY_NAME, name)
    }

    private fun printLog(key: String, value: Any = "") {
        val content = StringBuilder()
        content.append(key.padEnd(18)).append(": ").append(value.toString().padEnd(5))
        content.append(" - ")
        content.append(name.padEnd(14))
        content.append(" - ")
        val isVisible = isVisible
        appendLog(content, "isShown", view?.isShown, 5)
        appendLog(content, "isResumed", isResumed, 5)
        appendLog(content, "isVisible", isVisible, 5)
        if (!isVisible) {
            appendLog(content, "ViewVisibility", getVisibilityText(view?.visibility), 9)
            appendLog(content, "isAdded", isAdded, 5)
            appendLog(content, "isHidden", isHidden, 5)
            appendLog(content, "ViewWindowTokenNonNull", view?.windowToken != null, 5)
        }
        content.append("$this")
        LogUtil.i(content.toString())
    }

    private fun appendLog(content: StringBuilder, key: String, value: Any?, valueLength: Int) {
        content.append(key).append(": ").append(value.toString().padEnd(valueLength)).append(" ; ")
    }

    private fun getVisibilityText(visibility: Int?): String {
        return when (visibility) {
            View.VISIBLE -> "VISIBLE"
            View.INVISIBLE -> "INVISIBLE"
            View.GONE -> "GONE"
            else -> "UNKNOWN"
        }
    }

    /**
     * 获取调用者的方法名
     * level 为 0 时，获取当前调用者的方法名
     * level 为 1 时，获取调用当前方法的调用者的方法名
     * 以此类推
     */
    fun getCallerMethodName(level: Int = 0): String {
        val stackTrace = Thread.currentThread().stackTrace ?: return "UNKNOWN"
        val index = 3 + level
        val caller = stackTrace.getOrNull(index) ?: return "UNKNOWN"
        return caller.methodName
    }

}