package com.capstone.plasticwise.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.capstone.plasticwise.R
import com.capstone.plasticwise.Result
import com.capstone.plasticwise.ViewModelFactory
import com.capstone.plasticwise.databinding.FragmentDetectBinding
import com.capstone.plasticwise.utils.reduceFileImage
import com.capstone.plasticwise.utils.uriToFile
import com.capstone.plasticwise.viewModel.DetectViewModel
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.Locale

class DetectFragment : Fragment() {

    private lateinit var binding: FragmentDetectBinding
    private var currentImageUri: Uri? = null
    private val detectViewModel by viewModels<DetectViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                showToast("Permission granted")
            } else {
                showToast("Permission denied")
            }
        }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnDetect.setOnClickListener { detection() }
    }

    private fun detection() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireActivity()).reduceFileImage()
            Log.d("DetectFragment", "detection: ${imageFile.path}")
            detectViewModel.detection(imageFile).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        showToast("Loading...")
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showToast("Success")
                        showLoading(false)
                        val confidence = result.data.confidence
                        val roundedConfidence = String.format(Locale.US, "%.0f", confidence)
                        val resultCraft = result.data.data.result
                        binding.tvDetect.text = "$roundedConfidence % $resultCraft"
                        binding.tvMessage.text = result.data.data.message
                        binding.tvCraftNow.visibility = View.VISIBLE
                        binding.btnCraft.visibility = View.VISIBLE
                        binding.btnDetect.visibility = View.GONE

                        binding.btnCraft.setOnClickListener {
                            val intent = Intent(requireActivity(), CraftActivity::class.java)
                            intent.putExtra(CraftActivity.EXTRA_CATEGORIES, resultCraft)
                            startActivity(intent)
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            })
        } ?: showToast("No image selected")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }


    private val launcherIntentCameraX =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
                currentImageUri =
                    it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
                showImage()
            }
        }

    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                showToast("No Media Selected")
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showImage() {
        if (currentImageUri == null) {
            binding.ivDetect.setAnimation(R.raw.anim_empty)
        } else {
            currentImageUri?.let {
                val destinationUri =
                    Uri.fromFile(File(requireActivity().cacheDir, "croppedImage.jpg"))
                UCrop.of(it, destinationUri)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(800, 800)
                    .start(requireContext(), this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val resultUri = UCrop.getOutput(data!!)
                    resultUri?.let {
                        binding.ivDetect.setImageURI(it)
                        currentImageUri = it
                    }
                }

                UCrop.RESULT_ERROR -> {
                    val cropError = UCrop.getError(data!!)
                    cropError?.printStackTrace()
                    showToast("Crop error: ${cropError?.message}")
                }

                AppCompatActivity.RESULT_CANCELED -> {
                    showToast("Photo editing canceled")
                }
            }
        }
    }

    companion object {
        const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
