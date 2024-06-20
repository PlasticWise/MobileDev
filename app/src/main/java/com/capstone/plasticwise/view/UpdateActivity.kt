package com.capstone.plasticwise.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.data.remote.ResponsePostUserItem
import com.capstone.plasticwise.databinding.ActivityUpdateBinding
import com.capstone.plasticwise.utils.reduceFileImage
import com.capstone.plasticwise.utils.uriToFile
import com.capstone.plasticwise.viewModel.UpdateViewModel


class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private var currentImageUri: Uri? = null
    private val updateViewModel by viewModels<UpdateViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission Granted")
            } else {
                showToast("Permission Request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            UpdateActivity.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val categories = resources.getStringArray(R.array.categories_array)
        val categoriesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoriesAdapter

        val types = resources.getStringArray(R.array.types_array)
        val typesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = typesAdapter

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnUpload.setOnClickListener { updateStory() }
        binding.btnCancel.setOnClickListener { cancelAction() }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(UpdateActivity.REQUIRED_PERMISSION)
        }

        val image = intent.getStringExtra(PostDetailActivity.EXTRA_IMAGE)
        val title = intent.getStringExtra(PostDetailActivity.EXTRA_TITLE)
        val body = intent.getStringExtra(PostDetailActivity.EXTRA_BODY)
        val type = intent.getStringExtra(PostDetailActivity.TYPE)
        val categories1 = intent.getStringExtra(PostDetailActivity.CATEGORIES)

        binding.edtDescription.setText(body)
        binding.edtTitle.setText(title)
        binding.spinnerCategory.setSelection(categories.indexOf(categories1))
        binding.spinnerType.setSelection(types.indexOf(type))
        Glide.with(this)
            .load(image)
            .into(binding.ivUpload)
//        currentImageUri = image?.toUri()
    }

    private fun updateStory() {
        val image = intent.getStringExtra(PostDetailActivity.EXTRA_IMAGE)
        val id = intent.getStringExtra(PostDetailActivity.EXTRA_ID).toString()
        val title = binding.edtTitle.text.toString()
        val body = binding.edtDescription.text.toString()
        val categories = binding.spinnerCategory.selectedItem.toString()
        val type = binding.spinnerType.selectedItem.toString()
        if (image != null && currentImageUri == null) {
            val imageUri = image.toUri()
            if (imageUri.scheme == "file") {
                val fileImage = uriToFile(imageUri, this)
                updateViewModel.updateStory(id, fileImage, title, body, categories, type)
                    .observe(this) { result ->
                        handleResult(result)
                    }
            }
        } else {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                updateViewModel.updateStory(id, imageFile, title, body, categories, type)
                    .observe(this) { result ->
                        handleResult(result)
                    }
            }
        }
    }

    private fun handleResult(result: Result<ResponsePostUserItem>) {
        if (result != null) {
            when (result) {
                is Result.Loading -> {
                    showToast("Loading")
                }

                is Result.Success -> {
                    showToast("Success Update")
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }

                is Result.Error -> {
                    showToast("Error")
                }
            }
        }
    }


    private fun startCameraX() {
        val intent = Intent(this@UpdateActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image Uri", "showImage: $it")
            binding.ivUpload.setImageURI(it)
        }
    }

    private fun cancelAction() {
        binding.edtDescription.setText("")
        currentImageUri = null
        binding.ivUpload.setAnimation(R.raw.anim_upload)
        binding.ivUpload.playAnimation()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_BODY = "extra_body"
        const val CATEGORIES = "categories"
        const val TYPE = "type"
        const val EXTRA_ID = "extra_id"
    }
}


