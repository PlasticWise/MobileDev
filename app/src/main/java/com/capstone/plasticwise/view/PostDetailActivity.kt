package com.capstone.plasticwise.view

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.plasticwise.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        // Inisialisasi ImageView dengan findViewById
        val ivPostDetail = findViewById<ImageView>(R.id.ivPostDetail)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val fabUpdate = findViewById<FloatingActionButton>(R.id.fabUpdate)
        val fabDelete = findViewById<FloatingActionButton>(R.id.fabDelete)

        btnBack.setOnClickListener {
            finish()
        }
        fabUpdate.setOnClickListener {

        }

        fabDelete.setOnClickListener {

        }


            val imageResId = intent.getIntExtra("imageResId", R.drawable.ic_profile_user)

        Glide.with(this@PostDetailActivity)
            .load(imageResId)
            .transform(CenterCrop(), RoundedCornersTransformation(16, 0))
            .into(ivPostDetail)
    }
}
