package com.mask.customcomponents.fragment

import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment

/**
 * Fragment 基类
 * 实现对用户可见回调逻辑
 *
 * 注意：
 * 1、AndroidX.Fragment 版本号为 1.3.6 时，父 Fragment onHiddenChanged 方法调用时，不会同步调用子 Fragment 的 onHiddenChanged 方法。在 1.5.4 版本中，可以同步回调。
 *
 * Create by lishilin on 2025-07-24
 */
abstract class BaseFragment : Fragment() {

    // 是否对用户可见
    var isVisibleToUser = false
        private set

    // Fragment 是否显示
    private val isShown
        get() = run {
            isVisible && userVisibleHint && (view?.isShown == true)
        }

    private var viewTreeObserver: ViewTreeObserver? = null

    private var isOnGlobalLayoutListenerAdded = false

    private val onGlobalLayoutListener by lazy {
        ViewTreeObserver.OnGlobalLayoutListener {
            handleOnGlobalLayout()
        }
    }

    /************************************************************ S 系统类重写方法 ************************************************************/

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        handlerOnResume()
    }

    override fun onPause() {
        super.onPause()
        handleOnPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onHiddenChanged(isHidden: Boolean) {
        super.onHiddenChanged(isHidden)
        handleOnHiddenChanged(isHidden)
    }

    override fun setUserVisibleHint(isVisibleToUserHint: Boolean) {
        super.setUserVisibleHint(isVisibleToUserHint)
        handleSetUserVisibleHint(isVisibleToUserHint)
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
        dispatchOnVisibleToUser(isShown)

        onGlobalLayout()
    }

    private fun handlerOnResume() {
        if (isShown) {
            dispatchOnVisibleToUser(true)
        }

        addOnGlobalLayoutListener()
    }

    private fun handleOnPause() {
        removeOnGlobalLayoutListener()

        dispatchOnVisibleToUser(false)
    }

    private fun handleOnHiddenChanged(isHidden: Boolean) {
        if (isHidden) {
            dispatchOnVisibleToUser(false)
        } else {
            if (isShown) {
                dispatchOnVisibleToUser(true)
            }
        }
    }

    private fun handleSetUserVisibleHint(isVisibleToUserHint: Boolean) {
        if (isVisibleToUserHint) {
            if (isShown) {
                dispatchOnVisibleToUser(true)
            }
        } else {
            dispatchOnVisibleToUser(false)
        }
    }

    private fun addOnGlobalLayoutListener() {
        viewTreeObserver = view?.viewTreeObserver
        val viewTreeObserver = viewTreeObserver
        if (!isOnGlobalLayoutListenerAdded && viewTreeObserver?.isAlive == true) {
            isOnGlobalLayoutListenerAdded = true
            viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    private fun removeOnGlobalLayoutListener() {
        val viewTreeObserver = viewTreeObserver
        if (isOnGlobalLayoutListenerAdded && viewTreeObserver?.isAlive == true) {
            isOnGlobalLayoutListenerAdded = false
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
        this.viewTreeObserver = null
    }

    private fun dispatchOnVisibleToUser(isVisibleToUser: Boolean) {
        if (this.isVisibleToUser == isVisibleToUser) {
            return
        }
        this.isVisibleToUser = isVisibleToUser
        onVisibleToUser(isVisibleToUser)
    }

    /************************************************************ E 内部逻辑 ************************************************************/

}