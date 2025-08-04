package com.mask.customcomponents.fragment

import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment

/**
 * Fragment 基类
 * 实现对用户可见回调逻辑
 *
 * Create by lishilin on 2025-07-24
 */
abstract class BaseFragment : Fragment() {

    private var viewTreeObserver: ViewTreeObserver? = null

    private val onGlobalLayoutListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            onGlobalLayout()
        }
    }

    override fun onStart() {
        super.onStart()

        viewTreeObserver = view?.viewTreeObserver
        val viewTreeObserver = viewTreeObserver
        if (viewTreeObserver?.isAlive == true) {
            viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()

        val viewTreeObserver = viewTreeObserver
        if (viewTreeObserver?.isAlive == true) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
        this.viewTreeObserver = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    open fun onGlobalLayout() {
    }

}