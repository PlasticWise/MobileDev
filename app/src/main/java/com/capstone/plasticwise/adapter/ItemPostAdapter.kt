package com.capstone.plasticwise.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.plasticwise.R
import com.capstone.plasticwise.data.pref.ItemPost

class ItemPostAdapter(private val context: Context, private val itemList: List<ItemPost>) :
    RecyclerView.Adapter<ItemPostAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivStory)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = itemList[position]
                    val intent = Intent(context, PostDetailActivity::class.java)
                    intent.putExtra("imageResId", item.imageResId)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        Glide.with(context)
            .load(currentItem.imageResId)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount() = itemList.size
}
