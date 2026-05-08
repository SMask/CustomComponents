package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.adapter.RecyclerViewDividerAdapter
import com.mask.customcomponents.databinding.ActivityRecyclerViewDividerBinding
import com.mask.customcomponents.decoration.DividerGridItemDecoration
import com.mask.customcomponents.decoration.DividerItemDecoration
import com.mask.customcomponents.utils.SizeUtils

/**
 * RecyclerView Divider
 */
class RecyclerViewDividerActivity : AppCompatActivity() {

    private val mBinding by lazy {
        ActivityRecyclerViewDividerBinding.inflate(layoutInflater)
    }

    private val mLinearDivider by lazy {
        val size = SizeUtils.dpToPx(8)
        DividerItemDecoration.newInstance(size, 0x800000FF.toInt())
    }
    private val mLinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val mGridDivider by lazy {
        val width = SizeUtils.dpToPx(8)
        val height = SizeUtils.dpToPx(16)
        DividerGridItemDecoration.newInstance(width, height, 0x800000FF.toInt())
    }
    private val mGridLayoutManager by lazy {
        GridLayoutManager(this, 5)
    }

    private val mAdapter by lazy {
        val adapter = RecyclerViewDividerAdapter()
        val dataList = mutableListOf<Any>()
        repeat(52) {
            dataList.add(it)
        }
        adapter.setDataList(dataList)
        adapter
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, RecyclerViewDividerActivity::class.java)
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
        mBinding.rvLinear.layoutManager = mLinearLayoutManager
        mBinding.rvLinear.addItemDecoration(mLinearDivider)
        mBinding.rvLinear.adapter = mAdapter

        mBinding.rvGrid.layoutManager = mGridLayoutManager
        mBinding.rvGrid.addItemDecoration(mGridDivider)
        mBinding.rvGrid.adapter = mAdapter
    }

    private fun setListener() {
        mLinearDivider.setIgnoreStrategy(object : DividerItemDecoration.IIgnoreStrategy {
            override fun shouldIgnoreDivider(
                parent: RecyclerView,
                view: View,
                position: Int
            ): Boolean {
                return shouldDecorationIgnoreDivider(parent, view, position)
            }
        })

        mBinding.cbHorizontal.setOnCheckedChangeListener { _, isChecked ->
            switchOrientation(isChecked)
        }
        mBinding.cbIncludeEdge.setOnCheckedChangeListener { _, isChecked ->
            switchIncludeEdge(isChecked)
        }
        mBinding.cbIgnoreDivider.setOnCheckedChangeListener { _, isChecked ->
            switchIgnoreDivider(isChecked)
        }
    }

    private fun initData() {
        switchOrientation(false)
        switchIncludeEdge(false)
        switchIgnoreDivider(false)
    }

    private fun shouldDecorationIgnoreDivider(
        parent: RecyclerView,
        view: View,
        position: Int
    ): Boolean {
        if (!mBinding.cbIgnoreDivider.isChecked) {
            return false
        }
        return position % 2 == 0
    }

    private fun switchOrientation(isHorizontal: Boolean) {
        val orientation = if (isHorizontal) {
            RecyclerView.HORIZONTAL
        } else {
            RecyclerView.VERTICAL
        }
        mLinearLayoutManager.orientation = orientation
        mGridLayoutManager.orientation = orientation
        mAdapter.setOrientation(orientation)
    }

    private fun switchIncludeEdge(isIncludeEdge: Boolean) {
        mLinearDivider.isIncludeEdge = isIncludeEdge
        mGridDivider.isIncludeEdge = isIncludeEdge
        mAdapter.notifyDataSetChanged()
    }

    private fun switchIgnoreDivider(isIgnoreDivider: Boolean) {
        mAdapter.notifyDataSetChanged()
    }
}