package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.databinding.ActivityDraggableViewBinding
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.utils.ToastUtils
import com.mask.customcomponents.view.DragGestureDetector

/**
 * 可拖拽控件
 */
class DraggableViewActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDraggableViewBinding.inflate(layoutInflater)
    }

    private val dragGestureDetectorConstraint by lazy {
        DragGestureDetector(binding.viewConstraint)
    }

    private val dragGestureDetectorFrame by lazy {
        DragGestureDetector(binding.viewFrame)
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
        binding.viewConstraint.setOnClickListener {
            LogUtil.i("onClick ConstraintLayout")
            ToastUtils.show(this, "View ConstraintLayout")
        }
        binding.viewFrame.setOnClickListener {
            LogUtil.i("onClick FrameLayout")
            ToastUtils.show(this, "View FrameLayout")
        }
        binding.cbOutOfBounds.setOnCheckedChangeListener { _, isChecked ->
            dragGestureDetectorConstraint.setOutOfBoundsEnabled(isChecked)
            dragGestureDetectorFrame.setOutOfBoundsEnabled(isChecked)
        }
        binding.cbAnim.setOnCheckedChangeListener { _, isChecked ->
            dragGestureDetectorConstraint.setAnimEnabled(isChecked)
            dragGestureDetectorFrame.setAnimEnabled(isChecked)
        }
        binding.cbSnapHorizontal.setOnCheckedChangeListener { _, isChecked ->
            dragGestureDetectorConstraint.setHorizontalSnapEnabled(isChecked)
            dragGestureDetectorFrame.setHorizontalSnapEnabled(isChecked)
        }
        binding.cbSnapVertical.setOnCheckedChangeListener { _, isChecked ->
            dragGestureDetectorConstraint.setVerticalSnapEnabled(isChecked)
            dragGestureDetectorFrame.setVerticalSnapEnabled(isChecked)
        }

        dragGestureDetectorConstraint.attach()
        dragGestureDetectorFrame.attach()
    }

    private fun initData() {
        binding.cbOutOfBounds.setChecked(false)
        binding.cbAnim.setChecked(true)
        binding.cbSnapHorizontal.setChecked(true)
        binding.cbSnapVertical.setChecked(true)
    }
}
