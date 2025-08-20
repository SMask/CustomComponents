package com.mask.customcomponents.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.databinding.FragmentItemBinding
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.view.CountDownTimerHolder
import com.mask.customcomponents.view.CountDownTimerInfo
import com.mask.customcomponents.view.CountDownTimerListener

/**
 * Fragment Item
 *
 * Create by lishilin on 2025-07-24
 */
class ItemFragment : LogFragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(name: String): ItemFragment {
            val fragment = ItemFragment()
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
        binding.cdtvTime.setListener(object : CountDownTimerListener() {
            override fun onStart(info: CountDownTimerInfo) {
                super.onStart(info)
                LogUtil.i("Fragment $name onStart: $info")
            }

            override fun onTick(info: CountDownTimerInfo) {
                super.onTick(info)
                LogUtil.i("Fragment $name onTick: $info")
            }

            override fun onCancel(info: CountDownTimerInfo) {
                super.onCancel(info)
                LogUtil.i("Fragment $name onCancel: $info")
            }

            override fun onFinish(info: CountDownTimerInfo) {
                super.onFinish(info)
                LogUtil.i("Fragment $name onFinish: $info")
            }
        })
    }

    private fun initData() {
        binding.tvContent.text = name
        binding.tvContent.setTag(Global.Key.KEY_NAME.hashCode(), name)

        binding.cdtvTime.setTime(
            SystemClock.elapsedRealtime(),
            CountDownTimerHolder.MILLIS_DAY * 1 + CountDownTimerHolder.MILLIS_SECOND * 8
        )
    }

}