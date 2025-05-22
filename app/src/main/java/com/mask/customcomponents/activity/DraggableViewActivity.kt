package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityDraggableViewBinding

/**
 * 可拖拽控件
 */
class DraggableViewActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDraggableViewBinding.inflate(layoutInflater)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, DraggableViewActivity::class.java)
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
