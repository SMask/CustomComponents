package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityNumberTextAnimatorBinding
import com.mask.customcomponents.view.NumberTextAnimator
import kotlin.random.Random

/**
 * 数字动画
 */
class NumberTextAnimatorActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityNumberTextAnimatorBinding.inflate(layoutInflater)
    }

    private val numberTextAnimator by lazy {
        NumberTextAnimator()
    }

    private var targetNumber = 1000L

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, NumberTextAnimatorActivity::class.java)
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
        mBinding.btnSubtract.setOnClickListener {
            onClickSubtract()
        }
        mBinding.btnAdd.setOnClickListener {
            onClickAdd()
        }
    }

    private fun initData() {
        setTargetNumber(targetNumber)
    }

    private fun setTargetNumber(targetNumber: Long) {
        this.targetNumber = targetNumber
        numberTextAnimator.start(mBinding.tvNumber, targetNumber)
        mBinding.tvNumberTarget.text = targetNumber.toString()
    }

    private fun onClickSubtract() {
        val randomNumber = Random.nextLong(90, 110)
        setTargetNumber(targetNumber - randomNumber)
    }

    private fun onClickAdd() {
        val randomNumber = Random.nextLong(90, 110)
        setTargetNumber(targetNumber + randomNumber)
    }
}
