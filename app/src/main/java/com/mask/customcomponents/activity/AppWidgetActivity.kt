package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityAppWidgetBinding

/**
 * AppWidget 桌面小组件
 */
class AppWidgetActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityAppWidgetBinding.inflate(layoutInflater)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, AppWidgetActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
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