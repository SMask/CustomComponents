package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityNumberViewBinding

/**
 * 单个数字边框
 */
class NumberViewActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNumberViewBinding.inflate(layoutInflater)
    }

    private val numArr by lazy {
        intArrayOf(-87654321, -12345678, -123, -123456)
    }

    private var numIndex = 0

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val switchRunnable by lazy {
        Runnable {
            switchNumber()
        }
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, NumberViewActivity::class.java)
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun initView() {
    }

    private fun setListener() {
    }

    private fun initData() {
        switchNumber()
    }

    private fun switchNumber() {
        binding.numberView.setValue(numArr[numIndex])

        numIndex++
        if (numIndex >= numArr.size) {
            numIndex = 0
        }

        handler.postDelayed(switchRunnable, 1500)
    }
}
