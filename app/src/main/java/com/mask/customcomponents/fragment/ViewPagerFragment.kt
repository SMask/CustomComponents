package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
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
        arguments?.getSerializable(KEY_ADAPTER_TYPE) as? AdapterType ?: AdapterType.NORMAL_HINT
    }

    companion object {
        private const val KEY_ADAPTER_TYPE = "key_adapter_type"

        fun newInstance(adapterType: AdapterType): ViewPagerFragment {
            val fragment = ViewPagerFragment()
            fragment.setName("${adapterType.tab.tabName}Root")
            fragment.arguments?.putSerializable(KEY_ADAPTER_TYPE, adapterType)
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
        val itemNamePre = "${adapterType.tab.tabName}_Item_"
        val adapter = when (adapterType) {
            AdapterType.NORMAL_HINT -> {
                ItemViewPagerAdapter(
                    childFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
                    itemNamePre
                )
            }

            AdapterType.NORMAL_LIFECYCLE -> {
                ItemViewPagerAdapter(
                    childFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    itemNamePre
                )
            }

            AdapterType.STATE_HINT -> {
                ItemStateViewPagerAdapter(
                    childFragmentManager,
                    FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT,
                    itemNamePre
                )
            }

            AdapterType.STATE_LIFECYCLE -> {
                ItemStateViewPagerAdapter(
                    childFragmentManager,
                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                    itemNamePre
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

private class ItemViewPagerAdapter(
    fm: FragmentManager,
    behavior: Int,
    private val itemNamePre: String
) : FragmentPagerAdapter(fm, behavior) {

    override fun getItem(position: Int): Fragment {
        return ItemFragment.newInstance("$itemNamePre$position")
    }

    override fun getCount(): Int {
        return 9
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab_$position"
    }
}

private class ItemStateViewPagerAdapter(
    fm: FragmentManager,
    behavior: Int,
    private val itemNamePre: String
) : FragmentStatePagerAdapter(fm, behavior) {

    override fun getItem(position: Int): Fragment {
        return ItemFragment.newInstance("$itemNamePre$position")
    }

    override fun getCount(): Int {
        return 9
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab_$position"
    }
}