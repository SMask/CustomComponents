package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mask.customcomponents.R
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

    private val vpNHFragment by lazy {
        ViewPagerFragment.newInstance(ViewPagerFragment.AdapterType.NORMAL_HINT)
    }

    private val vpNLFragment by lazy {
        ViewPagerFragment.newInstance(ViewPagerFragment.AdapterType.NORMAL_LIFECYCLE)
    }

    private val vpSHFragment by lazy {
        ViewPagerFragment.newInstance(ViewPagerFragment.AdapterType.STATE_HINT)
    }

    private val vpSLFragment by lazy {
        ViewPagerFragment.newInstance(ViewPagerFragment.AdapterType.STATE_LIFECYCLE)
    }

    private val normalFragment by lazy {
        ItemFragment.newInstance("Normal_Item")
    }

    private val vp2Fragment by lazy {
        ViewPager2Fragment.newInstance()
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
        val currentFragment = currentFragment
        val targetFragment = getFragment(selectedTab)
        if (currentFragment === targetFragment) {
            return
        }
        this.currentFragment = targetFragment
        val transaction = childFragmentManager.beginTransaction()
        if (targetFragment.isAdded) {
            transaction.show(targetFragment)
        } else {
            transaction.add(R.id.layout_content, targetFragment)
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        transaction.commitAllowingStateLoss()
    }

    private fun getFragment(selectedTab: MainTab): Fragment {
        return when (selectedTab) {
            MainTab.ViewPagerNormalHint -> {
                vpNHFragment
            }

            MainTab.ViewPagerNormalLifecycle -> {
                vpNLFragment
            }

            MainTab.ViewPagerStateHint -> {
                vpSHFragment
            }

            MainTab.ViewPagerStateLifecycle -> {
                vpSLFragment
            }

            MainTab.Normal -> {
                normalFragment
            }

            MainTab.ViewPager2 -> {
                vp2Fragment
            }
        }
    }

}