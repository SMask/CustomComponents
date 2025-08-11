package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.databinding.FragmentViewPagerBinding
import com.mask.customcomponents.enums.MainTab

/**
 * Fragment ViewPager
 *
 * Create by lishilin on 2025-07-28
 */
class ViewPagerFragment : LogFragment() {

    enum class AdapterType(val tab: MainTab) {
        NORMAL_HINT(MainTab.ViewPagerNormalHint),
        NORMAL_LIFECYCLE(MainTab.ViewPagerNormalLifecycle),
        STATE_HINT(MainTab.ViewPagerStateHint),
        STATE_LIFECYCLE(MainTab.ViewPagerStateLifecycle),
    }

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private val adapterType by lazy {
        val adapterType = arguments?.getSerializable(Global.Key.KEY_ADAPTER_TYPE)
        adapterType as? AdapterType ?: AdapterType.NORMAL_HINT
    }

    private val hasPageChild by lazy {
        arguments?.getBoolean(Global.Key.KEY_HAS_CHILD_PAGE) ?: false
    }

    private val isChildPage by lazy {
        arguments?.getBoolean(Global.Key.KEY_IS_CHILD_PAGE) ?: false
    }

    companion object {
        fun newInstance(
            adapterType: AdapterType,
            hasChildPage: Boolean = false,
            isChildPage: Boolean = false
        ): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            val childPlaceholder = if (isChildPage) "Child" else ""
            fragment.setName("${adapterType.tab.tabName}${childPlaceholder}Root")
            fragment.arguments?.putSerializable(Global.Key.KEY_ADAPTER_TYPE, adapterType)
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
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        initView()
        setListener()
        initData()
        return binding.root
    }

    private fun initView() {
        val adapterDataProvider = object : VPAdapterDataProvider() {
            override fun getItem(position: Int): Fragment {
                if (hasPageChild && position == 4) {
                    return newInstance(adapterType, hasChildPage = false, isChildPage = true)
                }
                val childPlaceholder = if (isChildPage) "_C" else ""
                return ItemFragment.newInstance("${adapterType.tab.tabName}${childPlaceholder}_Item_$position")
            }
        }
        val adapter = when (adapterType) {
            AdapterType.NORMAL_HINT -> {
                ItemViewPagerAdapter(
                    childFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
                    adapterDataProvider
                )
            }

            AdapterType.NORMAL_LIFECYCLE -> {
                ItemViewPagerAdapter(
                    childFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    adapterDataProvider
                )
            }

            AdapterType.STATE_HINT -> {
                ItemStateViewPagerAdapter(
                    childFragmentManager,
                    FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
                    adapterDataProvider
                )
            }

            AdapterType.STATE_LIFECYCLE -> {
                ItemStateViewPagerAdapter(
                    childFragmentManager,
                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    adapterDataProvider
                )
            }
        }
        binding.vpContent.adapter = adapter

        binding.tabLayout.setupWithViewPager(binding.vpContent)
    }

    private fun setListener() {
    }

    private fun initData() {
    }

}

private abstract class VPAdapterDataProvider() {

    abstract fun getItem(position: Int): Fragment

    open fun getCount(): Int {
        return 9
    }

    open fun getPageTitle(position: Int): CharSequence? {
        return "Tab_$position"
    }
}

private class ItemViewPagerAdapter(
    fm: FragmentManager,
    behavior: Int,
    private val adapterDataProvider: VPAdapterDataProvider
) : FragmentPagerAdapter(fm, behavior) {

    override fun getItem(position: Int): Fragment {
        return adapterDataProvider.getItem(position)
    }

    override fun getCount(): Int {
        return adapterDataProvider.getCount()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return adapterDataProvider.getPageTitle(position)
    }
}

private class ItemStateViewPagerAdapter(
    fm: FragmentManager,
    behavior: Int,
    private val adapterDataProvider: VPAdapterDataProvider
) : FragmentStatePagerAdapter(fm, behavior) {

    override fun getItem(position: Int): Fragment {
        return adapterDataProvider.getItem(position)
    }

    override fun getCount(): Int {
        return adapterDataProvider.getCount()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return adapterDataProvider.getPageTitle(position)
    }
}