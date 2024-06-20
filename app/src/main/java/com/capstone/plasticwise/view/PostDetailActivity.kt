package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.data.remote.ResponsePostUserItem
import com.capstone.plasticwise.databinding.ActivityPostDetailBinding
import com.capstone.plasticwise.utils.DateFormatter
import com.capstone.plasticwise.viewModel.PostDetailActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding

    private val postViewModel by viewModels<PostDetailActivityViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ImageView dengan findViewById
        val ivPostDetail = findViewById<ImageView>(R.id.ivPostDetail)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val fabUpdate = findViewById<FloatingActionButton>(R.id.fabUpdate)
        val fabDelete = findViewById<FloatingActionButton>(R.id.fabDelete)

        btnBack.setOnClickListener {
            finish()
        }



        fabDelete.setOnClickListener {
            showConfirmationDialog()
        }


        val image = intent.getStringExtra(EXTRA_IMAGE)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val body = intent.getStringExtra(EXTRA_BODY)
        val categories = intent.getStringExtra(CATEGORIES)
        val type = intent.getStringExtra(TYPE)
        val createdAt = intent.getStringExtra(CREATED_AT)
        id = intent.getStringExtra(EXTRA_ID).toString()


        fabUpdate.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra(UpdateActivity.EXTRA_IMAGE, image)
            intent.putExtra(UpdateActivity.EXTRA_TITLE, title)
            intent.putExtra(UpdateActivity.EXTRA_BODY, body)
            intent.putExtra(UpdateActivity.CATEGORIES, categories)
            intent.putExtra(UpdateActivity.TYPE, type)
            intent.putExtra(UpdateActivity.EXTRA_ID, id)
            startActivity(intent)
        }
        binding.tvCreatedAt.text = DateFormatter(createdAt)
        binding.tvUser.text = title.toString()
        binding.tvCategories.text = categories
        binding.tvType.text = type
        binding.tvDescription.text = body

        Glide.with(this@PostDetailActivity)
            .load(image)
            .transform(CenterCrop(), RoundedCornersTransformation(16, 0))
            .into(ivPostDetail)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(this, androidx.appcompat.R.style.AlertDialog_AppCompat)
            .setTitle("Delete")
            .setMessage("Are you sure to Delete this Post?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                postViewModel.deletePostById(id).observe(this) { result ->
                    checkDelete(result, dialog.dismiss())
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun checkDelete(
        result: Result<ResponsePostUserItem>?,
        dismiss: Unit
    ) {
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                    dismiss
                }

                is Result.Error -> {
                    showToast("Error")
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }


    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_BODY = "extra_body"
        const val CATEGORIES = "categories"
        const val TYPE = "type"
        const val EXTRA_ID = "extra_id"
        const val CREATED_AT = "created_at"
    }
}
