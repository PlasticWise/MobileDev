package com.capstone.plasticwise.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.plasticwise.R
import com.capstone.plasticwise.data.pref.ItemPost

class ItemPostAdapter(private val itemList: List<ItemPost>) :
    RecyclerView.Adapter<ItemPostAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.imageView.setImageResource(currentItem.imageResId)
    }

    override fun getItemCount() = itemList.size
}
