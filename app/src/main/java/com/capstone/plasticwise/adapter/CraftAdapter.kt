package com.capstone.plasticwise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.plasticwise.data.remote.ListStoryItem
import com.capstone.plasticwise.data.remote.ResponseCraftingItem
import com.capstone.plasticwise.databinding.ItemCraftBinding

class CraftAdapter : ListAdapter<ResponseCraftingItem, CraftAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(val binding : ItemCraftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listCraft : ResponseCraftingItem) {
            Glide.with(itemView.context)
                .load(listCraft.imageUrl)
                .into(binding.iconImageView)

            binding.habitTitleTextView.text = listCraft.title
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResponseCraftingItem>() {
            override fun areItemsTheSame(oldItem: ResponseCraftingItem, newItem: ResponseCraftingItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ResponseCraftingItem,
                newItem: ResponseCraftingItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}