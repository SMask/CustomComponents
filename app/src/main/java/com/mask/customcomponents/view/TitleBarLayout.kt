package com.mask.customcomponents.view

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.mask.customcomponents.R
import com.mask.customcomponents.databinding.LayoutTitleBarLayoutBinding
import com.mask.customcomponents.utils.ActivityUtils
import com.mask.customcomponents.utils.StatusBarHelper

/**
 * 标题栏
 *
 * Create by lishilin on 2025-07-10
 */
class TitleBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val RES_NULL = -1

    private val mBinding by lazy {
        LayoutTitleBarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        initAttrs(context, attrs)
        setListener()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        try {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout)
            typedArray.use {
                val isFitStatusBar =
                    typedArray.getBoolean(R.styleable.TitleBarLayout_fitStatusBar, true)
                if (isFitStatusBar && !isInEditMode) {
                    StatusBarHelper.setPaddingTop(this)
                }

                // S 返回图标
                val backVisibility =
                    typedArray.getInt(R.styleable.TitleBarLayout_backVisibility, View.VISIBLE)
                mBinding.imgBack.visibility = backVisibility

                val backRes = typedArray.getResourceId(R.styleable.TitleBarLayout_backRes, RES_NULL)
                if (backRes != RES_NULL) {
                    setBackImageRes(backRes)
                }

                val backTint =
                    typedArray.getColorStateList(R.styleable.TitleBarLayout_backTint)
                if (backTint != null) {
                    setBackTint(backTint)
                }
                // E 返回图标

                // S 标题
                val titleVisibility =
                    typedArray.getInt(R.styleable.TitleBarLayout_titleVisibility, View.VISIBLE)
                mBinding.tvTitle.visibility = titleVisibility

                val titleText = typedArray.getText(R.styleable.TitleBarLayout_titleText)
                setTitleText(
                    if (titleText.isNullOrEmpty()) {
                        getActivityTitle()
                    } else {
                        titleText
                    }
                )

                val titleTextColor =
                    typedArray.getColorStateList(R.styleable.TitleBarLayout_titleTextColor)
                if (titleTextColor != null) {
                    setTitleTextColor(titleTextColor)
                }
                // E 标题

                // S 操作按钮
                val actionBtnVisibility =
                    typedArray.getInt(R.styleable.TitleBarLayout_actionBtnVisibility, View.GONE)
                mBinding.tvAction.visibility = actionBtnVisibility

                val actionBtnText = typedArray.getText(R.styleable.TitleBarLayout_actionBtnText)
                setActionBtnText(actionBtnText)

                val actionBtnTextColor =
                    typedArray.getColorStateList(R.styleable.TitleBarLayout_actionBtnTextColor)
                if (actionBtnTextColor != null) {
                    setActionBtnTextColor(actionBtnTextColor)
                }
                // E 操作按钮

                // S 操作图标
                val actionIconVisibility =
                    typedArray.getInt(R.styleable.TitleBarLayout_actionIconVisibility, View.GONE)
                mBinding.imgAction.visibility = actionIconVisibility

                val actionIconRes =
                    typedArray.getResourceId(R.styleable.TitleBarLayout_actionIconRes, RES_NULL)
                if (actionIconRes != RES_NULL) {
                    setActionIconImageRes(actionIconRes)
                }

                val actionIconTint =
                    typedArray.getColorStateList(R.styleable.TitleBarLayout_actionIconTint)
                if (actionIconTint != null) {
                    setActionIconTint(actionIconTint)
                }
                // E 操作图标
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setListener() {
        setBackOnClickListener {
            onBackClick()
        }
    }

    private fun getActivityTitle(): String {
        try {
            val componentName = ActivityUtils.getActivity(context)?.componentName ?: return ""
            val packageManager = context.packageManager ?: return ""
            val activityInfo =
                packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            return activityInfo.loadLabel(packageManager).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun onBackClick() {
        ActivityUtils.getActivity(context)?.onBackPressed()
    }

    /************************************************************ S 外部调用 ************************************************************/

    /**** S 返回图标 ****/
    fun setBackVisible(isVisible: Boolean) {
        mBinding.imgBack.isVisible = isVisible
    }

    fun setBackImageRes(@DrawableRes resId: Int) {
        mBinding.imgBack.setImageResource(resId)
    }

    fun setBackTint(@ColorInt color: Int) {
        setBackTint(ColorStateList.valueOf(color))
    }

    fun setBackTint(tint: ColorStateList?) {
        mBinding.imgBack.setImageTintList(tint)
    }

    fun setBackOnClickListener(listener: OnClickListener?) {
        mBinding.imgBack.setOnClickListener(listener)
    }
    /**** E 返回图标 ****/

    /**** S 标题 ****/
    fun setTitleVisible(isVisible: Boolean) {
        mBinding.tvTitle.isVisible = isVisible
    }

    fun setTitleText(text: CharSequence?) {
        mBinding.tvTitle.text = text
    }

    fun setTitleText(@StringRes resId: Int) {
        mBinding.tvTitle.setText(resId)
    }

    fun setTitleTextColor(@ColorInt color: Int) {
        mBinding.tvTitle.setTextColor(color)
    }

    fun setTitleTextColor(color: ColorStateList) {
        mBinding.tvTitle.setTextColor(color)
    }
    /****  E 标题 ****/

    /**** S 操作按钮 ****/
    fun setActionBtnVisible(isVisible: Boolean) {
        mBinding.tvAction.isVisible = isVisible
    }

    fun setActionBtnEnabled(isEnabled: Boolean) {
        mBinding.tvAction.isEnabled = isEnabled
    }

    fun setActionBtnText(text: CharSequence?) {
        mBinding.tvAction.text = text
    }

    fun setActionBtnText(@StringRes resId: Int) {
        mBinding.tvAction.setText(resId)
    }

    fun setActionBtnTextColor(@ColorInt color: Int) {
        mBinding.tvAction.setTextColor(color)
    }

    fun setActionBtnTextColor(color: ColorStateList) {
        mBinding.tvAction.setTextColor(color)
    }

    fun setActionBtnOnClickListener(listener: OnClickListener?) {
        mBinding.tvAction.setOnClickListener(listener)
    }
    /**** E 操作按钮 ****/

    /**** S 操作图标 ****/
    fun setActionIconVisible(isVisible: Boolean) {
        mBinding.imgAction.isVisible = isVisible
    }

    fun setActionIconImageRes(@DrawableRes resId: Int) {
        mBinding.imgAction.setImageResource(resId)
    }

    fun setActionIconTint(@ColorInt color: Int) {
        setActionIconTint(ColorStateList.valueOf(color))
    }

    fun setActionIconTint(tint: ColorStateList?) {
        mBinding.imgAction.setImageTintList(tint)
    }

    fun setActionIconOnClickListener(listener: OnClickListener?) {
        mBinding.imgAction.setOnClickListener(listener)
    }
    /**** E 操作图标 ****/

    /************************************************************ E 外部调用 ************************************************************/

}