package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.mask.customcomponents.databinding.FragmentTabBinding
import com.mask.customcomponents.databinding.ItemRbTabBinding
import com.mask.customcomponents.enums.MainTab
import com.mask.customcomponents.viewmodel.FragmentVisibilityViewModel

/**
 * Fragment Tab
 *
 * Create by lishilin on 2025-07-24
 */
class TabFragment : LogFragment() {

    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding!!

    private var selectedTab = MainTab.DEFAULT // 当前选中的 Tab

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[FragmentVisibilityViewModel::class.java]
    }

    companion object {
        fun newInstance(): TabFragment {
            val fragment = TabFragment()
            fragment.setName("TabRoot")
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabBinding.inflate(inflater, container, false)
        initView()
        setListener()
        initData()
        return binding.root
    }

    private fun initView() {
        MainTab.entries.forEach { tab ->
            addRadioButton(binding.rgTab, tab.tabName)
        }
    }

    private fun setListener() {
        binding.rgTab.setOnCheckedChangeListener { group, checkedId ->
            val index = binding.rgTab.children.indexOfFirst { it.id == checkedId }
            setSelectedTab(MainTab.getInstance(index, selectedTab))
        }

        viewModel.selectedTab.observe(viewLifecycleOwner) { tab ->
            if (tab == null) {
                return@observe
            }
            val rbTab = binding.rgTab.getChildAt(tab.ordinal)
            if (rbTab is RadioButton) {
                rbTab.isChecked = true
            }
        }
    }

    private fun initData() {
        setSelectedTab(MainTab.DEFAULT)
    }

    private fun addRadioButton(rgGroup: RadioGroup, text: String) {
        val rbBinding = ItemRbTabBinding.inflate(layoutInflater, rgGroup, true)
        rbBinding.root.id = View.generateViewId()
        rbBinding.root.text = text
    }

    private fun setSelectedTab(selectedTab: MainTab) {
        this.selectedTab = selectedTab
        viewModel.setSelectedTab(selectedTab)
    }

}