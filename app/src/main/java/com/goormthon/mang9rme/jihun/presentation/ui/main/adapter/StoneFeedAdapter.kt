package com.goormthon.mang9rme.jihun.presentation.ui.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.databinding.ItemMainRvContentsBinding
import com.goormthon.mang9rme.databinding.ItemMainRvWhenNullBinding
import net.daum.mf.map.api.MapView


class StoneFeedAdapter(private val onFeedClick : ((Int) -> Unit)? = null) : ListAdapter<StoneData, RecyclerView.ViewHolder>(
    mDiffUtil()
) {

    inner class StoneFeedViewHolder(val binding : ItemMainRvContentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : StoneData, onFeedClick: ((Int) -> Unit)?) {
            binding.stoneData = item
            binding.root.setOnClickListener { onFeedClick?.invoke(item.stoneId) }
        }
    }

    inner class StoneFeedNullViewHolder(val binding : ItemMainRvWhenNullBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
          0 -> StoneFeedNullViewHolder(ItemMainRvWhenNullBinding.inflate(LayoutInflater.from(parent.context), parent, false))
          else -> StoneFeedViewHolder(ItemMainRvContentsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int = if(currentList.size == 0) 0 else 1
    override fun getItemCount(): Int = if(currentList.size == 0) 1 else currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is StoneFeedViewHolder -> holder.bind(getItem(position), onFeedClick)

            is StoneFeedNullViewHolder -> holder.bind()
        }
    }

    private class mDiffUtil : DiffUtil.ItemCallback<StoneData>() {
        override fun areItemsTheSame(oldItem: StoneData, newItem: StoneData): Boolean {
            return oldItem.stoneId == newItem.stoneId
        }

        override fun areContentsTheSame(oldItem: StoneData, newItem: StoneData): Boolean {
            return oldItem == newItem
        }
    }
}