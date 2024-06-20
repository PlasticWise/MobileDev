package com.capstone.plasticwise.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityDetailBinding
import com.capstone.plasticwise.utils.DateFormatter
import com.capstone.plasticwise.viewModel.DetailViewModel
import com.google.firebase.auth.FirebaseAuth
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val id = intent.getStringExtra(EXTRA_ID)

        getDetail(id.toString())

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getDetail(id: String) {
        detailViewModel.getDetail(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showToast("Loading")
                    }
                    is Result.Success -> {
                        showToast("Success")
                        val title = result.data.title
                        val photoUrl = result.data.imageUrl
                        val detail = result.data.body
                        val type = result.data.type
                        val categories = result.data.categories
                        val createdAt = result.data.createdAt
                        showDetail(title, photoUrl, detail, type, categories, createdAt)
                    }
                    is Result.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showDetail(username: String?, photoUrl: String?, detail: String?, type: String?, categories: String?, createdAt: String?) {
        binding.apply {
            tvUser.text = username
            tvDescription.setIndentedText(detail ?: "")
            tvType.text = type
            tvCategories.text = categories
            tvCreatedAt.text = DateFormatter(createdAt)
            Glide.with(this@DetailActivity)
                .load(photoUrl)
                .transform(CenterCrop(), RoundedCornersTransformation(16, 0))
                .into(ivDetail)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
