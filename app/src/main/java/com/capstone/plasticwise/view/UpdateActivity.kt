package com.capstone.plasticwise.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.plasticwise.view.CameraActivity.Companion.CAMERAX_RESULT
import com.capstone.plasticwise.view.CameraActivity.Companion.EXTRA_CAMERAX_IMAGE
import com.capstone.plasticwise.R
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityUpdateBinding
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
//        binding.btnUpload.setOnClickListener { updateStory() }
        binding.btnCancel.setOnClickListener { cancelAction() }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(UpdateActivity.REQUIRED_PERMISSION)
        }


    }



    //    private fun updateStory() {
//        currentImageUri?.let { uri ->
//            val imageFile = uriToFile(uri, this).reduceFileImage()
//            Log.d("Image File", "show image: ${imageFile.path}")
//            val title = binding.edtTitle.text.toString()
//            val body = binding.edtDescription.text.toString()
//            val categories = binding.spinnerCategory.selectedItem.toString()
//            val type = binding.spinnerType.selectedItem.toString()
//            val authorId = auth.uid.toString()
//
//            updateViewModel.updateStory(imageFile, title, body, categories, type, authorId)
//                .observe(this) { result ->
//                    if (result != null) {
//                        when (result) {
//                            is Result.Loading -> {
//                                showToast("Loading...")
//                            }
//
//                            is Result.Success -> {
//                                showToast("Success")
//                                val intent = Intent(this@UpdateActivity, HomeActivity::class.java)
//                                intent.flags =
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                startActivity(intent)
//                            }
//
//                            is Result.Error -> {
//                                showToast(result.error)
//                            }
//                        }
//                    }
//                }
//        }
//    }

    private val requestPermissionLauncherLocation =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            when {
                permission[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    showToast("Permission granted fine")
                }

                permission[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    showToast("Permission granted coarse")
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
    }
}


