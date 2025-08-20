package com.mask.customcomponents.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.databinding.ItemCountDownTimerBinding
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.utils.ToastUtils
import com.mask.customcomponents.view.CountDownTimerInfo
import com.mask.customcomponents.view.CountDownTimerListener
import com.mask.customcomponents.vo.CountDownTimerVo

/**
 * 倒计时
 *
 * Create by lishilin on 2025-08-20
 */
class CountDownTimerAdapter : RecyclerView.Adapter<CountDownTimerAdapter.ViewHolder>() {

    private val dataList = mutableListOf<CountDownTimerVo>()

    private fun getItem(position: Int): CountDownTimerVo? {
        return dataList.getOrNull(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCountDownTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        holder.binding.tvIndex.text = position.toString()

        val startTime = data?.startTime ?: 0L
        val remainingTimeForStart = data?.remainingTimeForStart ?: 0L
        holder.binding.cdtvTime.setTime(startTime, remainingTimeForStart)
    }

    inner class ViewHolder(
        val binding: ItemCountDownTimerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val data = getItem(bindingAdapterPosition)
                val msg = "$data"
                ToastUtils.show(itemView.context, msg)
            }

            binding.cdtvTime.setListener(object : CountDownTimerListener() {
                override fun onStart(info: CountDownTimerInfo) {
                    super.onStart(info)
                    LogUtil.i("Adapter $bindingAdapterPosition onStart: $info")
                }

                override fun onTick(info: CountDownTimerInfo) {
                    super.onTick(info)
//                    LogUtil.i("Adapter $bindingAdapterPosition onTick: $info")
                }

                override fun onCancel(info: CountDownTimerInfo) {
                    super.onCancel(info)
                    LogUtil.i("Adapter $bindingAdapterPosition onCancel: $info")
                }

                override fun onFinish(info: CountDownTimerInfo) {
                    super.onFinish(info)
                    LogUtil.i("Adapter $bindingAdapterPosition onFinish: $info")
                }
            })
        }
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setDataList(dataList: MutableList<CountDownTimerVo>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    /************************************************************ E 外部调用 ************************************************************/
}