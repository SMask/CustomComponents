package com.mask.customcomponents.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.config.Global
import com.mask.customcomponents.databinding.ItemCountDownTimerBinding
import com.mask.customcomponents.utils.CommonUtils
import com.mask.customcomponents.utils.LogUtil
import com.mask.customcomponents.utils.ToastUtils
import com.mask.customcomponents.view.CountDownTimerHolder.Companion.MILLIS_DAY
import com.mask.customcomponents.view.CountDownTimerHolder.Companion.MILLIS_HOUR
import com.mask.customcomponents.view.CountDownTimerHolder.Companion.MILLIS_MINUTE
import com.mask.customcomponents.view.CountDownTimerHolder.Companion.MILLIS_SECOND
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
        holder.binding.tvIndex.setTag(Global.Key.KEY_NAME.hashCode(), "Adapter_$position")

        val startTime = data?.startTime ?: 0L
        val remainingTimeForStart = data?.remainingTimeForStart ?: 0L
        holder.binding.cdtvTime.setTime(startTime, remainingTimeForStart)

        val remainingDays = remainingTimeForStart / MILLIS_DAY
        val remainingHours = remainingTimeForStart % MILLIS_DAY / MILLIS_HOUR
        val remainingMinutes = remainingTimeForStart % MILLIS_HOUR / MILLIS_MINUTE
        val remainingSeconds = remainingTimeForStart % MILLIS_MINUTE / MILLIS_SECOND
        holder.binding.tvTime.text =
            "${remainingDays}天${remainingHours}:${remainingMinutes}:${remainingSeconds}"
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
                    printLog(bindingAdapterPosition, "onStart", info)
                }

                override fun onTick(info: CountDownTimerInfo) {
                    super.onTick(info)
//                    printLog(bindingAdapterPosition, "onTick", info)
                }

                override fun onCancel(info: CountDownTimerInfo) {
                    super.onCancel(info)
                    printLog(bindingAdapterPosition, "onCancel", info)
                }

                override fun onFinish(info: CountDownTimerInfo) {
                    super.onFinish(info)
                    printLog(bindingAdapterPosition, "onFinish", info)
                }
            })
        }
    }

    private fun printLog(position: Int, key: String, value: Any) {
        val content = StringBuilder()
        content.append("Adapter").append(" ")
        content.append(position.toString().padEnd(2)).append(" ")
        content.append(key.padEnd(8)).append(" - ")

        val callerMethodName = CommonUtils.getCallerMethodName(8)
        content.append("Caller").append(": ").append(callerMethodName.padEnd(20)).append(" - ")

        content.append(value.toString())
        LogUtil.i(content.toString())
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setDataList(dataList: MutableList<CountDownTimerVo>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    /************************************************************ E 外部调用 ************************************************************/
}