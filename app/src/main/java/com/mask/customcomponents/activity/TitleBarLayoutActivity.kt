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
        binding.layoutTitle2.setBackVisible(false)
    }

    private fun setListener() {
        binding.layoutTitle1.setBackOnClickListener {
            ToastUtils.show("自定义返回按钮")
        }
        binding.layoutTitle1.setActionOnClickListener {
            ToastUtils.show("自定义操作按钮")
        }

        binding.layoutDown.setOnClickListener {
            ToastUtils.show("自定义操作View")
        }
    }

    private fun initData() {
    }

}
