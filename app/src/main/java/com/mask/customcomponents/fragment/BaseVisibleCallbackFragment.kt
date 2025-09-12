package com.mask.customcomponents.fragment

import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment

/**
 * Fragment 实现对用户可见回调逻辑基类
 *
 * 兼容 add、remove、replace、show、hide；
 * 兼容 ViewPager，BEHAVIOR_SET_USER_VISIBLE_HINT 及 BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT；
 * 兼容 ViewPager2；
 * 兼容 ViewPager 嵌套场景；
 * 兼容 Fragment 低版本的两个问题；
 *
 * AndroidX.Fragment 版本号 1.3.6 与 1.5.4 差异点：
 * 1、在 1.3.6 版本，父 Fragment.onHiddenChanged 方法调用时，不会同步调用子 Fragment.onHiddenChanged 方法。在 1.5.4 版本，可以同步回调。
 * 2、在 1.3.6 版本，子 Fragment.isHidden 方法调用时，不会同步判断父 Fragment.isHidden 方法。在 1.5.4 版本，可以同步回调。
 *
 * Create by lishilin on 2025-07-24
 */
abstract class BaseVisibleCallbackFragment : Fragment() {

    // 是否对用户可见
    var isVisibleToUser = false
        private set

    // 是否第一次对用户可见
    var isFirstOnVisibleToUser = true
        private set

    // Fragment 是否显示
    private val isShown
        get() = run {
            isVisible && !isHiddenIncludeParent() && isUserVisibleHintIncludeParent() && (view?.isShown == true) && isResumed
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
        handleOnResume()
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

    private fun handleOnResume() {
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

    private fun isHiddenIncludeParent(): Boolean {
        return isHidden || isParentHidden()
    }

    private fun isParentHidden(): Boolean {
        val parentFragment = parentFragment
        if (parentFragment == null) {
            return false
        }
        if (parentFragment is BaseVisibleCallbackFragment) {
            return parentFragment.isHiddenIncludeParent()
        }
        return parentFragment.isHidden
    }

    private fun isUserVisibleHintIncludeParent(): Boolean {
        return userVisibleHint && isParentUserVisibleHint()
    }

    private fun isParentUserVisibleHint(): Boolean {
        val parentFragment = parentFragment
        if (parentFragment == null) {
            return true
        }
        if (parentFragment is BaseVisibleCallbackFragment) {
            return parentFragment.isUserVisibleHintIncludeParent()
        }
        return parentFragment.userVisibleHint
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
        if (isVisibleToUser) {
            isFirstOnVisibleToUser = false
        }
        dispatchChildOnVisibleToUser(isVisibleToUser)
    }

    private fun dispatchChildOnVisibleToUser(isVisibleToUser: Boolean) {
        if (host == null) {
            return
        }
        for (childFragment in childFragmentManager.fragments) {
            if (childFragment !is BaseVisibleCallbackFragment) {
                continue
            }
            if (isVisibleToUser) {
                if (childFragment.isShown) {
                    childFragment.dispatchOnVisibleToUser(true)
                }
            } else {
                childFragment.dispatchOnVisibleToUser(false)
            }
        }
    }

    /************************************************************ E 内部逻辑 ************************************************************/

}