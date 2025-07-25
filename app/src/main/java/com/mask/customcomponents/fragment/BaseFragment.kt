package com.mask.customcomponents.fragment

import androidx.fragment.app.Fragment

/**
 * Fragment 基类
 * 实现对用户可见回调逻辑
 *
 * Create by lishilin on 2025-07-24
 */
abstract class BaseFragment : Fragment() {

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

}