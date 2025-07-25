package com.mask.customcomponents.fragment

import android.os.Bundle
import com.mask.customcomponents.utils.LogUtil

/**
 * Fragment 打印日志
 *
 * Create by lishilin on 2025-07-24
 */
abstract class LogFragment : BaseFragment() {

    private val name by lazy {
        arguments?.getString(KEY_NAME)
    }

    companion object {
        private const val KEY_NAME = "key_name"
    }

    override fun onStart() {
        super.onStart()
        LogUtil.i("onStart: $name $this")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.i("onResume: $name $this")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.i("onPause: $name $this")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.i("onStop: $name $this")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        LogUtil.i("onHiddenChanged: $hidden $name $this")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        LogUtil.i("setUserVisibleHint: $isVisibleToUser $name $this")
    }

    fun setName(name: String) {
        if (arguments == null) {
            arguments = Bundle()
        }
        arguments?.putString(KEY_NAME, name)
    }

}