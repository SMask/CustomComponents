package com.mask.customcomponents.fragment

import android.os.Bundle
import com.mask.customcomponents.utils.LogUtil

/**
 * Fragment 打印日志
 *
 * Create by lishilin on 2025-07-24
 */
abstract class LogFragment : BaseFragment() {

    protected val name by lazy {
        arguments?.getString(KEY_NAME) ?: "name"
    }

    companion object {
        private const val KEY_NAME = "key_name"
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        printLog("onHiddenChanged", hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        printLog("setUserVisibleHint", isVisibleToUser)
    }

    protected fun setName(name: String) {
        if (arguments == null) {
            arguments = Bundle()
        }
        arguments?.putString(KEY_NAME, name)
    }

    private fun printLog(key: String, value: Any = "") {
        val content = StringBuilder()
        content.append(key.padEnd(18)).append(": ")
        content.append(value.toString().padEnd(5)).append(" - ")
        content.append(name.padEnd(12)).append(" ")
        content.append("$this")
        LogUtil.i(content.toString())
    }

}