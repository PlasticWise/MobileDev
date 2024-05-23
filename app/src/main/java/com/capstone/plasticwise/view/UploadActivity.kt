package com.capstone.plasticwise.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.ActivityUploadBinding
import com.capstone.plasticwise.utils.reduceFileImage
import com.capstone.plasticwise.utils.uriToFile
import com.capstone.plasticwise.view.CameraActivity.Companion.CAMERAX_RESULT
import com.capstone.plasticwise.view.CameraActivity.Companion.EXTRA_CAMERAX_IMAGE
import com.capstone.plasticwise.viewModel.UploadViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private val uploadViewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun permissionLocationGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        if (!permissionLocationGranted()) {
            requestPermissionLauncherLocation.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnGallery.setOnClickListener { startGallery() }
//        binding.btnCamera.setOnClickListener { startCameraX() }

        binding.btnUpload.setOnClickListener { uploadImage(0.0, 0.0) }

        binding.switchFeature.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val lon = location.longitude
                            val lat = location.latitude
                            Log.d("Location", "onCreate: $lat $lon")
                            showToast("Feature enabled")
                            binding.btnUpload.setOnClickListener { uploadImage(lat, lon) }
                        } else {
                            showToast("Location is null")
                        }
                    }
                } else {
                    showToast("Permission not granted")
                }
            } else {
                showToast("Feature disabled")
                binding.btnUpload.setOnClickListener { uploadImage(0.0, 0.0) }
            }
        }
        binding.btnCancel.setOnClickListener { cancelAction() }
    }


    private val requestPermissionLauncherLocation =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            when {
                permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    showToast("Permission granted fine")
                }

                permission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    showToast("Permission granted coarse")
                }
            }
        }


    private fun startCameraX() {
        val intent = Intent(this@UploadActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage(lat: Double, long: Double) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "show image: ${imageFile.path}")
            val description = binding.edtDescription.text.toString()

            uploadViewModel.uploadImage(imageFile, description, lat, long).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showToast("Loading...")
                            Log.d("Loading", "uploadImage: $lat, $long")
                        }

                        is Result.Success -> {
                            showToast(result.data.message)
                            val intent = Intent(this@UploadActivity, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                        is Result.Error -> {
                            showToast(result.error)
                        }
                    }
                }
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
        binding.ivUpload.setImageResource(R.drawable.background_upload)
    }
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
