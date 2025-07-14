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

    private val binding by lazy {
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

                val backVisibility =
                    typedArray.getInt(R.styleable.TitleBarLayout_backVisibility, View.VISIBLE)
                binding.imgBack.visibility = backVisibility

                val backRes = typedArray.getResourceId(R.styleable.TitleBarLayout_backRes, RES_NULL)
                if (backRes != RES_NULL) {
                    setBackImageRes(backRes)
                }

                val backTint =
                    typedArray.getColorStateList(R.styleable.TitleBarLayout_backTint)
                if (backTint != null) {
                    setBackTint(backTint)
                }

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

                val actionVisibility =
                    typedArray.getInt(R.styleable.TitleBarLayout_actionVisibility, View.GONE)
                binding.tvAction.visibility = actionVisibility

                val actionText = typedArray.getText(R.styleable.TitleBarLayout_actionText)
                setActionText(actionText)

                val actionTextColor =
                    typedArray.getColorStateList(R.styleable.TitleBarLayout_actionTextColor)
                if (actionTextColor != null) {
                    setActionTextColor(actionTextColor)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setListener() {
        binding.imgBack.setOnClickListener {
            finish()
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

    private fun finish() {
        ActivityUtils.getActivity(context)?.finish()
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setBackVisible(isVisible: Boolean) {
        binding.imgBack.isVisible = isVisible
    }

    fun setBackImageRes(@DrawableRes resId: Int) {
        binding.imgBack.setImageResource(resId)
    }

    fun setBackTint(@ColorInt color: Int) {
        setBackTint(ColorStateList.valueOf(color))
    }

    fun setBackTint(tint: ColorStateList?) {
        binding.imgBack.setImageTintList(tint)
    }

    fun setBackOnClickListener(listener: OnClickListener) {
        binding.imgBack.setOnClickListener(listener)
    }

    fun setTitleText(title: CharSequence?) {
        binding.tvTitle.text = title
    }

    fun setTitleText(@StringRes resId: Int) {
        binding.tvTitle.setText(resId)
    }

    fun setTitleTextColor(@ColorInt color: Int) {
        binding.tvTitle.setTextColor(color)
    }

    fun setTitleTextColor(color: ColorStateList) {
        binding.tvTitle.setTextColor(color)
    }

    fun setActionVisible(isVisible: Boolean) {
        binding.tvAction.isVisible = isVisible
    }

    fun setActionText(title: CharSequence?) {
        binding.tvAction.text = title
    }

    fun setActionText(@StringRes resId: Int) {
        binding.tvAction.setText(resId)
    }

    fun setActionTextColor(@ColorInt color: Int) {
        binding.tvAction.setTextColor(color)
    }

    fun setActionTextColor(color: ColorStateList) {
        binding.tvAction.setTextColor(color)
    }

    fun setActionOnClickListener(listener: OnClickListener) {
        binding.tvAction.setOnClickListener(listener)
    }

    /************************************************************ E 外部调用 ************************************************************/

}