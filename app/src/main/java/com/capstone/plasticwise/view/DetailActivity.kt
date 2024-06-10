package com.capstone.plasticwise.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityDetailBinding
import com.capstone.plasticwise.viewModel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        detailViewModel.getDetail(id.toString()).observe(this) {result ->
            if (result != null){
                when(result) {
                    is Result.Loading -> {
                        showToast("Opened")
                    }
                    is Result.Success -> {
                        val username = result.data.story?.name
                        val photoUrl = result.data.story?.photoUrl
                        val detail = result.data.story?.description
                        showDetail(username, photoUrl, detail)
                    }
                    is Result.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showDetail(username: String?, photoUrl: String?, detail: String?) {
        binding.apply {
            tvUser.text = username
            tvDescription.text = detail
            Glide.with(this@DetailActivity)
                .load(photoUrl)
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