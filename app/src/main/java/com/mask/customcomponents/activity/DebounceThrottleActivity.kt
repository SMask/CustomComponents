package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityDebounceThrottleBinding
import com.mask.customcomponents.utils.DebounceThrottleUtils
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.utils.ToastUtils
import com.mask.customcomponents.utils.addDebounceTextChangedListener
import com.mask.customcomponents.utils.setOnDebounceClickListener
import com.mask.customcomponents.utils.setOnThrottleClickListener

/**
 * 防抖、节流
 */
class DebounceThrottleActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityDebounceThrottleBinding.inflate(layoutInflater)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, DebounceThrottleActivity::class.java)
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
        val onDebounceClickListener = View.OnClickListener { view ->
            val content = (view as? TextView)?.text ?: ""
            val delayMillis = when (view) {
                mBinding.btnDebounce500 -> {
                    500L
                }

                mBinding.btnDebounce1000 -> {
                    1000L
                }

                mBinding.btnDebounce2000 -> {
                    2000L
                }

                else -> {
                    return@OnClickListener
                }
            }
            LogUtil.i("onDebounceClick content: $content, delayMillis: $delayMillis")
            DebounceThrottleUtils.debounce(view, delayMillis) {
                LogUtil.i("onDebounceClick debounce content: $content, delayMillis: $delayMillis")
            }
        }
//        mBinding.btnDebounce500.setOnClickListener(onDebounceClickListener)
//        mBinding.btnDebounce1000.setOnClickListener(onDebounceClickListener)
//        mBinding.btnDebounce2000.setOnClickListener(onDebounceClickListener)
        mBinding.btnDebounce500.setOnDebounceClickListener(onDebounceClickListener)
        mBinding.btnDebounce1000.setOnDebounceClickListener(onDebounceClickListener)
        mBinding.btnDebounce2000.setOnDebounceClickListener(onDebounceClickListener)

        val onThrottleClickListener = View.OnClickListener { view ->
            val content = (view as? TextView)?.text ?: ""
            val intervalMillis = when (view) {
                mBinding.btnThrottle500 -> {
                    500L
                }

                mBinding.btnThrottle1000 -> {
                    1000L
                }

                mBinding.btnThrottle2000 -> {
                    2000L
                }

                else -> {
                    return@OnClickListener
                }
            }
            LogUtil.i("onThrottleClick content: $content, intervalMillis: $intervalMillis")
            DebounceThrottleUtils.throttle(view, intervalMillis) {
                LogUtil.i("onThrottleClick throttle content: $content, intervalMillis: $intervalMillis")
            }
        }
//        mBinding.btnThrottle500.setOnClickListener(onThrottleClickListener)
//        mBinding.btnThrottle1000.setOnClickListener(onThrottleClickListener)
//        mBinding.btnThrottle2000.setOnClickListener(onThrottleClickListener)
//        mBinding.btnThrottle500.setOnThrottleClickListener {
//            LogUtil.i("onThrottleClick content: btnThrottle500")
//        }
        mBinding.btnThrottle500.setOnThrottleClickListener(onThrottleClickListener)
        mBinding.btnThrottle1000.setOnThrottleClickListener(onThrottleClickListener)
        mBinding.btnThrottle2000.setOnThrottleClickListener(onThrottleClickListener)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
//                LogUtil.i("beforeTextChanged text: $text, start: $start, count: $count, after: $after")
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
//                LogUtil.i("onTextChanged text: $text, start: $start, before: $before, count: $count")
            }

            override fun afterTextChanged(text: Editable?) {
                LogUtil.i("afterTextChanged text: $text")
                DebounceThrottleUtils.debounce(mBinding.edtDebounce500, 500L) {
                    LogUtil.i("afterTextChanged debounce text: $text")
                    ToastUtils.show("afterTextChanged debounce text: $text")
                }
            }
        }
//        mBinding.edtDebounce500.addTextChangedListener(textWatcher)
        mBinding.edtDebounce500.addDebounceTextChangedListener(textWatcher)
    }

    private fun initData() {
    }
}