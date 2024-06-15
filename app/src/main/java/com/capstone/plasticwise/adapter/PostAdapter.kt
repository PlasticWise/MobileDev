package com.capstone.plasticwise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.plasticwise.data.remote.ResponsePostUserItem
import com.capstone.plasticwise.databinding.ItemHomeBinding
import com.capstone.plasticwise.databinding.ItemPostBinding
import com.capstone.plasticwise.viewModel.PostViewModel

class PostAdapter : ListAdapter<ResponsePostUserItem, PostAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResponsePostUserItem) {
            binding.apply {
                tvName.text = data.title
                tvDescription.text = data.body
            }
            Glide.with(itemView.context)
                .load(data.imageUrl)
                .into(binding.ivStory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.MyViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostAdapter.MyViewHolder, position: Int) {
        val listPost = getItem(position)
        holder.bind(listPost)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResponsePostUserItem>() {
            override fun areItemsTheSame(
                oldItem: ResponsePostUserItem,
                newItem: ResponsePostUserItem
            ): Boolean {
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