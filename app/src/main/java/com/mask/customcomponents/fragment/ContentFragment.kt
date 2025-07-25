package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mask.customcomponents.databinding.FragmentContentBinding
import com.mask.customcomponents.enums.MainTab
import com.mask.customcomponents.viewmodel.FragmentVisibilityViewModel

/**
 * Fragment 内容
 *
 * Create by lishilin on 2025-07-24
 */
class ContentFragment : LogFragment() {

    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    private var currentFragment: Fragment? = null

    private val tabFragment1 by lazy {
        ItemFragment.newInstance(MainTab.TAB_1.tabName)
    }

    private val tabFragment2 by lazy {
        ItemFragment.newInstance(MainTab.TAB_2.tabName)
    }

    private val tabFragment3 by lazy {
        ItemFragment.newInstance(MainTab.TAB_3.tabName)
    }

    private val tabFragment4 by lazy {
        ItemFragment.newInstance(MainTab.TAB_4.tabName)
    }

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[FragmentVisibilityViewModel::class.java]
    }

    companion object {
        fun newInstance(): ContentFragment {
            val fragment = ContentFragment()
            fragment.setName("ContentRoot")
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        initView()
        setListener()
        initData()
        return binding.root
    }

    private fun initView() {
    }

    private fun setListener() {
        viewModel.selectedTab.observe(viewLifecycleOwner) { tab ->
            if (tab == null) {
                return@observe
            }
            switchFragment(tab)
        }
    }

    private fun initData() {
    }

    private fun switchFragment(selectedTab: MainTab) {
        val fragment = getFragment(selectedTab)
        if (fragment.isVisible) {
            return
        }
        val currentFragment = currentFragment
        if (currentFragment === fragment) {
            return
        }
    }

    private fun getFragment(selectedTab: MainTab): Fragment {
        return when (selectedTab) {
            MainTab.TAB_1 -> {
                tabFragment1
            }

            MainTab.TAB_2 -> {
                tabFragment2
            }

            MainTab.TAB_3 -> {
                tabFragment3
            }

            MainTab.TAB_4 -> {
                tabFragment4
            }
        }
    }

}