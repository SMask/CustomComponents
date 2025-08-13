package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityNumberAnimViewBinding

/**
 * 数字动画控件
 */
class NumberAnimViewActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNumberAnimViewBinding.inflate(layoutInflater)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, NumberAnimViewActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setListener()
        initData()
    }

    private fun initView() {
    }

    private fun setListener() {
    }

    private fun initData() {
    }
}
