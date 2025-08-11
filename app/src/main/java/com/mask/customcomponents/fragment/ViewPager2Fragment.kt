package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.databinding.FragmentViewPager2Binding
import com.mask.customcomponents.enums.MainTab

/**
 * Fragment ViewPager2
 *
 * Create by lishilin on 2025-07-30
 */
class ViewPager2Fragment : LogFragment() {

    private var _binding: FragmentViewPager2Binding? = null
    private val binding get() = _binding!!

    private val hasPageChild by lazy {
        arguments?.getBoolean(Global.Key.KEY_HAS_CHILD_PAGE) ?: false
    }

    private val isChildPage by lazy {
        arguments?.getBoolean(Global.Key.KEY_IS_CHILD_PAGE) ?: false
    }

    companion object {
        private val tab = MainTab.ViewPager2

        fun newInstance(
            hasChildPage: Boolean = false,
            isChildPage: Boolean = false
        ): ViewPager2Fragment {
            val fragment = ViewPager2Fragment()
            val childPlaceholder = if (isChildPage) "Child" else ""
            fragment.setName("${tab.tabName}${childPlaceholder}Root")
            fragment.arguments?.putBoolean(Global.Key.KEY_HAS_CHILD_PAGE, hasChildPage)
            fragment.arguments?.putBoolean(Global.Key.KEY_IS_CHILD_PAGE, isChildPage)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPager2Binding.inflate(inflater, container, false)
        initView()
        setListener()
        initData()
        return binding.root
    }

    private fun initView() {
        val adapterDataProvider = object : VP2AdapterDataProvider() {
            override fun createFragment(position: Int): Fragment {
                if (hasPageChild && position == 4) {
                    return newInstance(hasChildPage = false, isChildPage = true)
                }
                val childPlaceholder = if (isChildPage) "_C" else ""
                return ItemFragment.newInstance("${tab.tabName}${childPlaceholder}_Item_$position")
            }
        }
        binding.vpContent.adapter = ItemViewPager2Adapter(this, adapterDataProvider)

        TabLayoutMediator(binding.tabLayout, binding.vpContent) { tab, position ->
            tab.text = "Tab_$position"
        }.attach()
    }

    private fun setListener() {
    }

    private fun initData() {
    }

}

private abstract class VP2AdapterDataProvider() {

    abstract fun createFragment(position: Int): Fragment

    open fun getItemCount(): Int {
        return 9
    }
}

private class ItemViewPager2Adapter(
    fragment: Fragment,
    private val adapterDataProvider: VP2AdapterDataProvider
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return adapterDataProvider.createFragment(position)
    }

    override fun getItemCount(): Int {
        return adapterDataProvider.getItemCount()
    }

}