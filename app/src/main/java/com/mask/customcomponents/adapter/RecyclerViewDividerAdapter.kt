package com.mask.customcomponents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mask.customcomponents.R
import com.mask.customcomponents.databinding.ItemSizeByHorizontalBinding
import com.mask.customcomponents.databinding.ItemSizeByVerticalBinding
import com.mask.customcomponents.view.SizeView

/**
 * RecyclerView Divider
 *
 * Create by lishilin on 2026-01-23
 */
class RecyclerViewDividerAdapter : RecyclerView.Adapter<RecyclerViewDividerAdapter.ViewHolder>() {

    private val mDataList = mutableListOf<Any>()

    private var mOrientation = RecyclerView.VERTICAL

    override fun getItemViewType(position: Int): Int {
        return mOrientation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = if (viewType == RecyclerView.VERTICAL) {
            ItemSizeByVerticalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        } else {
            ItemSizeByHorizontalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSize.setPosition(position)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    class ViewHolder(
        val view: View
    ) : RecyclerView.ViewHolder(view) {
        val tvSize: SizeView = view.findViewById(R.id.tv_size)
    }

    /************************************************************ S 外部调用 ************************************************************/

    fun setDataList(dataList: MutableList<Any>) {
        this.mDataList.clear()
        this.mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setOrientation(@RecyclerView.Orientation orientation: Int) {
        this.mOrientation = orientation
        notifyDataSetChanged()
    }

    /************************************************************ E 外部调用 ************************************************************/
}