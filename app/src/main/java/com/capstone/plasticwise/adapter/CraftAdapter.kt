package com.capstone.plasticwise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.plasticwise.data.remote.ListStoryItem
import com.capstone.plasticwise.databinding.ItemCraftBinding

class CraftAdapter : ListAdapter<ListStoryItem, CraftAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(val binding : ItemCraftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listCraft : ListStoryItem) {
            Glide.with(itemView.context)
                .load(listCraft.photoUrl)
                .into(binding.iconImageView)

            binding.habitTitleTextView.text = listCraft.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CraftAdapter.ListViewHolder {
        val binding = ItemCraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CraftAdapter.ListViewHolder, position: Int) {
        val listCraft = getItem(position)
        holder.bind(listCraft)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}