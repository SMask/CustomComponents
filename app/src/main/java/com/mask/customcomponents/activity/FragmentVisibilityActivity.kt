package com.mask.customcomponents.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mask.customcomponents.R
import com.mask.customcomponents.databinding.ActivityFragmentVisibilityBinding
import com.mask.customcomponents.fragment.ContentFragment
import com.mask.customcomponents.fragment.TabFragment

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
        setContentView(binding.root)
        initView()
        setListener()
        initData()
    }

    private fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_content, contentFragment)
            .replace(R.id.layout_tab, tabFragment)
            .commitAllowingStateLoss()
    }

    private fun setListener() {
    }

    private fun initData() {
    }

}
