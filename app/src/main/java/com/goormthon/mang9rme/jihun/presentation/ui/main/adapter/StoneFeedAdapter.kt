package com.goormthon.mang9rme.jihun.presentation.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goormthon.mang9rme.common.data.StoneData
import com.goormthon.mang9rme.databinding.ItemMainRvContentsBinding
import net.daum.mf.map.api.MapView


class StoneFeedAdapter(private val context : Context, private val onFeedClick : ((Int) -> Unit)? = null) : ListAdapter<StoneData, StoneFeedAdapter.StoneFeedViewHolder>(
    mDiffUtil()
) {

    inner class StoneFeedViewHolder(val binding : ItemMainRvContentsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : StoneData, onFeedClick: ((Int) -> Unit)?) {
            binding.stoneData = item
            binding.root.setOnClickListener { onFeedClick?.invoke(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoneFeedViewHolder {
        return StoneFeedViewHolder(ItemMainRvContentsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoneFeedViewHolder, position: Int) {
        holder.bind(getItem(position), onFeedClick)
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