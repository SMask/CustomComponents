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

/**
 * Fragment ViewPager
 *
 * Create by lishilin on 2025-07-28
 */
class ViewPagerFragment : LogFragment() {

    enum class AdapterType(val typeName: String) {
        NORMAL_HINT("NormalHint"),
        NORMAL_LIFECYCLE("NormalLifecycle"),
        STATE_HINT("StateHint"),
        STATE_LIFECYCLE("StateLifecycle"),
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
            fragment.setName("VPRoot_${adapterType.typeName}")
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
        val adapter = when (adapterType) {
            AdapterType.NORMAL_HINT -> {
                ItemViewPagerAdapter(
                    childFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
                )
            }

            AdapterType.NORMAL_LIFECYCLE -> {
                ItemViewPagerAdapter(
                    childFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                )
            }

            AdapterType.STATE_HINT -> {
                ItemStateViewPagerAdapter(
                    childFragmentManager,
                    FragmentStatePagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
                )
            }

            AdapterType.STATE_LIFECYCLE -> {
                ItemStateViewPagerAdapter(
                    childFragmentManager,
                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
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

private class ItemViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {

    private val itemName by lazy {
        when (behavior) {
            BEHAVIOR_SET_USER_VISIBLE_HINT -> "ViewPager_Normal_Hint_Item_"
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT -> "ViewPager_Normal_Lifecycle_Item_"
            else -> "ViewPager_Normal_Item_"
        }
    }

    override fun getItem(position: Int): Fragment {
        return ItemFragment.newInstance("$itemName$position")
    }

    override fun getCount(): Int {
        return 7
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab_$position"
    }
}

private class ItemStateViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {

    private val itemName by lazy {
        when (behavior) {
            BEHAVIOR_SET_USER_VISIBLE_HINT -> "ViewPager_State_Hint_Item_"
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT -> "ViewPager_State_Lifecycle_Item_"
            else -> "ViewPager_State_Item_"
        }
    }

    override fun getItem(position: Int): Fragment {
        return ItemFragment.newInstance("$itemName$position")
    }

    override fun getCount(): Int {
        return 9
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab_$position"
    }
}