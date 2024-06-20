package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.plasticwise.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            val intent = Intent(this, UpdateActivity::class.java)
            startActivity(intent)
        }


        fabDelete.setOnClickListener {
            showConfirmationDialog()
        }


            val imageResId = intent.getIntExtra("imageResId", R.drawable.ic_profile_user)

        Glide.with(this@PostDetailActivity)
            .load(imageResId)
            .transform(CenterCrop(), RoundedCornersTransformation(16, 0))
            .into(ivPostDetail)
    }
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(this, androidx.appcompat.R.style.AlertDialog_AppCompat)
            .setTitle("Delete")
            .setMessage("Are you sure to Delete this Post?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
