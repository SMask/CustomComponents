package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mask.customcomponents.databinding.FragmentViewPager2Binding

/**
 * Fragment ViewPager2
 *
 * Create by lishilin on 2025-07-30
 */
class ViewPager2Fragment : LogFragment() {

    private var _binding: FragmentViewPager2Binding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ViewPager2Fragment {
            val fragment = ViewPager2Fragment()
            fragment.setName("ViewPager2Root")
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
        binding.vpContent.adapter = ItemViewPager2Adapter(this)

        TabLayoutMediator(binding.tabLayout, binding.vpContent) { tab, position ->
            tab.text = "Tab_$position"
        }.attach()
    }

    private fun setListener() {
    }

    private fun initData() {
    }

}

private class ItemViewPager2Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return ItemFragment.newInstance("ViewPager2_Item_$position")
    }

    override fun getItemCount(): Int {
        return 9
    }

}