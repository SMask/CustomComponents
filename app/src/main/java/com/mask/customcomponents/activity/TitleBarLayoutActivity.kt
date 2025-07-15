package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityTitleBarLayoutBinding
import com.mask.customcomponents.utils.StatusBarHelper
import com.mask.customcomponents.utils.ToastUtils

/**
 * 标题栏
 */
class TitleBarLayoutActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityTitleBarLayoutBinding.inflate(layoutInflater)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, TitleBarLayoutActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusBarHelper.setStatusBarTranslucent(window)
        StatusBarHelper.setStatusBarDarkMode(window, true)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setListener()
        initData()
    }

    private fun initView() {
        binding.layoutTitle4.setBackVisible(false)
    }

    private fun setListener() {
        binding.layoutTitle1.setBackOnClickListener {
            ToastUtils.show("1 自定义返回按钮")
        }
        binding.layoutTitle1.setActionBtnOnClickListener {
            ToastUtils.show("1 自定义操作按钮")
        }
        binding.layoutTitle1.setActionIconOnClickListener {
            ToastUtils.show("1 自定义操作图标")
        }

        binding.layoutTitle2.setBackOnClickListener {
            ToastUtils.show("2 自定义返回按钮")
        }
        binding.layoutTitle2.setActionIconOnClickListener {
            ToastUtils.show("2 自定义操作图标")
        }

        binding.layoutTitle3.setBackOnClickListener {
            ToastUtils.show("3 自定义返回按钮")
        }
        binding.layoutTitle3.setActionBtnOnClickListener {
            ToastUtils.show("3 自定义操作按钮")
        }

        binding.layoutCustom.setOnClickListener {
            ToastUtils.show("自定义添加View")
        }
    }

    private fun initData() {
    }

}
