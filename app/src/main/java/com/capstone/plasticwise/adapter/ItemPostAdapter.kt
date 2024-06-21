package com.capstone.plasticwise.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.plasticwise.data.remote.ResponsePostUserItem
import com.capstone.plasticwise.databinding.ItemPostBinding
import com.capstone.plasticwise.view.PostDetailActivity

class ItemPostAdapter :
    ListAdapter<ResponsePostUserItem, ItemPostAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listPost: ResponsePostUserItem) {
            Glide.with(itemView.context)
                .load(listPost.imageUrl)
                .into(binding.ivStory)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PostDetailActivity::class.java)
                val title = listPost.title
                val body = listPost.body
                val categories = listPost.categories
                val type = listPost.type
                val createdAt = listPost.createdAt
                intent.putExtra(PostDetailActivity.EXTRA_IMAGE, listPost.imageUrl)
                intent.putExtra(PostDetailActivity.EXTRA_TITLE, title)
                intent.putExtra(PostDetailActivity.EXTRA_BODY, body)
                intent.putExtra(PostDetailActivity.CATEGORIES, categories)
                intent.putExtra(PostDetailActivity.TYPE, type)
                intent.putExtra(PostDetailActivity.CREATED_AT, createdAt)
                intent.putExtra(PostDetailActivity.EXTRA_ID, listPost.id)
                itemView.context.startActivity(intent)

            }
        }

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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
