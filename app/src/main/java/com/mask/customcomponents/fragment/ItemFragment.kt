package com.mask.customcomponents.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mask.customcomponents.databinding.FragmentItemBinding

/**
 * Fragment Item
 *
 * Create by lishilin on 2025-07-24
 */
class ItemFragment : LogFragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(name: String): TabFragment {
            val fragment = TabFragment()
            fragment.setName(name)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        initView()
        setListener()
        initData()
        return binding.root
    }

    private fun initView() {
    }

    private fun setListener() {
    }

    private fun initData() {
        binding.tvContent.text = tag
    }

}