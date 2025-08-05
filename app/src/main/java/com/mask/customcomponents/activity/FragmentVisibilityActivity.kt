package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.R
import com.mask.customcomponents.databinding.ActivityFragmentVisibilityBinding
import com.mask.customcomponents.fragment.ContentFragment
import com.mask.customcomponents.fragment.TabFragment
import com.mask.customcomponents.utils.LogUtil

/**
 * Fragment对用户可见回调
 */
class FragmentVisibilityActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFragmentVisibilityBinding.inflate(layoutInflater)
    }

    private val contentFragment by lazy {
        ContentFragment.newInstance()
    }

    private val tabFragment by lazy {
        TabFragment.newInstance()
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, FragmentVisibilityActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.i("onCreate Activity")
        setContentView(binding.root)
        initView()
        setListener()
        initData()
    }

    override fun onStart() {
        super.onStart()
        LogUtil.i("onStart Activity")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.i("onResume Activity")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.i("onPause Activity")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.i("onStop Activity")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.i("onDestroy Activity")
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_tab, tabFragment)
            .replace(R.id.layout_content, contentFragment)
            .commitAllowingStateLoss()
    }

    private fun setListener() {
        binding.layoutTitle.setActionBtnOnClickListener {
            showDialog()
        }
    }

    private fun initData() {
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setTitle("自定义弹窗标题")
            .setMessage("自定义弹窗内容")
            .setNeutralButton(R.string.sure, null)
            .show()
    }

}
