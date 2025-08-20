package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mask.customcomponents.adapter.CountDownTimerAdapter
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.databinding.ActivityCountDownTimerBinding
import com.mask.customcomponents.decoration.DividerItemDecoration
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.utils.SizeUtils
import com.mask.customcomponents.view.CountDownTimerHolder
import com.mask.customcomponents.view.CountDownTimerInfo
import com.mask.customcomponents.view.CountDownTimerListener
import com.mask.customcomponents.vo.CountDownTimerVo

/**
 * 倒计时（天时分秒）
 */
class CountDownTimerActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityCountDownTimerBinding.inflate(layoutInflater)
    }

    private val dataList by lazy {
        val dataList = mutableListOf<CountDownTimerVo>()
        repeat(100) { index ->
            dataList.add(CountDownTimerVo(CountDownTimerHolder.MILLIS_SECOND * 10 * index))
        }
        dataList
    }

    private val mAdapter by lazy {
        CountDownTimerAdapter()
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, CountDownTimerActivity::class.java)
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
        mBinding.rvContent.layoutManager = LinearLayoutManager(this)
        mBinding.rvContent.addItemDecoration(
            DividerItemDecoration.getInstance(SizeUtils.dpToPx(8))
        )
        mBinding.rvContent.adapter = mAdapter
    }

    private fun setListener() {
        mBinding.cdtvTime.setListener(object : CountDownTimerListener() {
            override fun onStart(info: CountDownTimerInfo) {
                super.onStart(info)
                LogUtil.i("Activity onStart: $info")
            }

            override fun onTick(info: CountDownTimerInfo) {
                super.onTick(info)
                LogUtil.i("Activity onTick: $info")
            }

            override fun onCancel(info: CountDownTimerInfo) {
                super.onCancel(info)
                LogUtil.i("Activity onCancel: $info")
            }

            override fun onFinish(info: CountDownTimerInfo) {
                super.onFinish(info)
                LogUtil.i("Activity onFinish: $info")
            }
        })
    }

    private fun initData() {
        mBinding.tvVisible.setTag(Global.Key.KEY_NAME.hashCode(), "Activity")

        mBinding.cdtvTime.setTime(
            SystemClock.elapsedRealtime(),
            CountDownTimerHolder.MILLIS_SECOND * 15
        )
        mAdapter.setDataList(dataList)
    }
}
