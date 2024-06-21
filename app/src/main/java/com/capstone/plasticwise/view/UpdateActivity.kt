package com.capstone.plasticwise.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.data.remote.ResponsePostUserItem
import com.capstone.plasticwise.databinding.ActivityUpdateBinding
import com.capstone.plasticwise.utils.reduceFileImage
import com.capstone.plasticwise.utils.uriToFile
import com.capstone.plasticwise.viewModel.UpdateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private var currentImageFile: File? = null
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
            Manifest.permission.CAMERA
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
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val body = intent.getStringExtra(EXTRA_BODY)
        val type = intent.getStringExtra(TYPE)
        val categories1 = intent.getStringExtra(CATEGORIES)

        binding.edtDescription.setText(body)
        binding.edtTitle.setText(title)
        binding.spinnerCategory.setSelection(categories.indexOf(categories1))
        binding.spinnerType.setSelection(types.indexOf(type))

        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivUpload)

        if (imageUrl != null) {
            downloadImage(imageUrl,
                onSuccess = { file ->
                    currentImageFile = file
                },
                onError = { error ->
                    showToast("Failed to download image: ${error.message}")
                }
            )
        }
    }

    private fun downloadImage(
        url: String,
        onSuccess: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val file = Glide.with(this@UpdateActivity)
                    .asFile()
                    .load(url)
                    .submit()
                    .get()

                withContext(Dispatchers.Main) {
                    onSuccess(file)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    private fun updateStory() {
        val id = intent.getStringExtra(EXTRA_ID).toString()
        val title = binding.edtTitle.text.toString()
        val body = binding.edtDescription.text.toString()
        val categories = binding.spinnerCategory.selectedItem.toString()
        val type = binding.spinnerType.selectedItem.toString()

        currentImageFile?.let { imageFile ->
            val reducedImageFile = imageFile.reduceFileImage()
            updateViewModel.updateStory(id, reducedImageFile, title, body, categories, type)
                .observe(this) { result ->
                    handleResult(result)
                }
        } ?: showToast("No image selected")
    }

    private fun handleResult(result: Result<ResponsePostUserItem>) {
        when (result) {
            is Result.Loading -> {
                showToast("Loading")
                showLoading(true)
            }
            is Result.Success -> {
                showToast("Success Update")
                showLoading(false)
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            is Result.Error -> {
                showLoading(false)
                showToast("Error")
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

    private fun startCameraX() {
        val intent = Intent(this@UpdateActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.let { imageUri ->
                currentImageFile = File(imageUri)
                showImage()
            }
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
    ) { uri ->
        uri?.let {
            currentImageFile = uriToFile(it, this).reduceFileImage()
            showImage()
        }
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private fun showImage() {
        currentImageFile?.let {
            mainHandler.post {
                Glide.with(this)
                    .load(it)
                    .into(binding.ivUpload)
            }
        }
    }

    private fun cancelAction() {
        binding.edtDescription.setText("")
        currentImageFile = null
        binding.ivUpload.setAnimation(R.raw.anim_upload)
        binding.ivUpload.playAnimation()
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_BODY = "extra_body"
        const val CATEGORIES = "categories"
        const val TYPE = "type"
        const val EXTRA_ID = "extra_id"
    }
}
