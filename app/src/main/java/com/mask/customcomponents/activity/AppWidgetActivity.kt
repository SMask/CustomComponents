package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.databinding.ActivityAppWidgetBinding
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.widget.AppWidgetHelper

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
        mBinding.btnAdd.setOnClickListener {
            val result = AppWidgetHelper.requestPinAppWidget(this)
            LogUtil.i("${Global.Tag.APP_WIDGET} requestPinAppWidget: $result")
        }
        mBinding.btnRefresh.setOnClickListener {
            AppWidgetHelper.updateAppWidget(this, "App Activity Btn")
        }
    }

    private fun initData() {
    }
}