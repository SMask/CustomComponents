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

    var isVisibleToUser = false // 是否对用户可见
        private set

    private var viewTreeObserver: ViewTreeObserver? = null

    private val onGlobalLayoutListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            handleOnGlobalLayout()
            onGlobalLayout()
        }
    }

    /************************************************************ S 系统类重写方法 ************************************************************/

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

        if (isVisible) {
            dispatchVisibleToUser(true)
        }
    }

    override fun onPause() {
        super.onPause()

        dispatchVisibleToUser(false)
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

    /************************************************************ E 系统类重写方法 ************************************************************/

    /************************************************************ S 自定义重写方法 ************************************************************/

    open fun onGlobalLayout() {
    }

    open fun onVisibleToUser(isVisibleToUser: Boolean) {
    }

    /************************************************************ E 自定义重写方法 ************************************************************/

    /************************************************************ S 内部逻辑 ************************************************************/

    private fun handleOnGlobalLayout() {
        dispatchVisibleToUser(isVisible)
    }

    private fun dispatchVisibleToUser(isVisibleToUser: Boolean) {
        if (this.isVisibleToUser == isVisibleToUser) {
            return
        }
        this.isVisibleToUser = isVisibleToUser
        onVisibleToUser(isVisibleToUser)
    }

    /************************************************************ E 内部逻辑 ************************************************************/

}