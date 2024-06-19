package com.capstone.plasticwise.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.plasticwise.data.remote.ListStoryItem
import com.capstone.plasticwise.data.remote.ResponsePostUserItem
import com.capstone.plasticwise.databinding.ItemHomeBinding
import com.capstone.plasticwise.view.DetailActivity

class StoryAdapter : ListAdapter<ResponsePostUserItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem : ResponsePostUserItem) {
            Glide.with(itemView.context)
                .load(listItem.imageUrl)
                .into(binding.ivStory)

            binding.tvName.text = listItem.title
            binding.tvDescription.text = listItem.body
            binding.tvType.text =listItem.type
            binding.tvCategories.text=listItem.categories

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, listItem.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listItem = getItem(position)
        if (listItem != null) {
            holder.bind(listItem)
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ResponsePostUserItem>(){
            override fun areItemsTheSame(oldItem: ResponsePostUserItem, newItem: ResponsePostUserItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ResponsePostUserItem,
                newItem: ResponsePostUserItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}