package com.mask.customcomponents.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.databinding.ItemUserListBinding
import com.mask.customcomponents.utils.ToastUtils
import com.mask.customcomponents.vo.UserVo

/**
 * 用户列表
 *
 * Create by lishilin on 2025-05-19
 */
class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val dataList = mutableListOf<UserVo>()

    private fun getItem(position: Int): UserVo? {
        return dataList.getOrNull(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        holder.binding.tvName.text = data?.name
    }

    inner class ViewHolder(val binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val data = getItem(bindingAdapterPosition)
                val msg = "${data?.id} - ${data?.name}"
                ToastUtils.show(itemView.context, msg)
            }
        }
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setDataList(dataList: MutableList<UserVo>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    /************************************************************ E 外部调用 ************************************************************/
}