package com.capstone.plasticwise.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.capstone.plasticwise.R
import com.capstone.plasticwise.databinding.FragmentDetectBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetectFragment : Fragment() {

    private lateinit var binding: FragmentDetectBinding

    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

//    PERMISSION
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                showToast("Permission granted")
            }
            else {
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.topAppBar.apply {
            setTitleTextColor(resources.getColor(R.color.green))
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.history -> {
                        Toast.makeText(requireActivity(), "History", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }
        val btnCraft: Button = view.findViewById(R.id.btnCraft)
        btnCraft.setOnClickListener {
            val intent = Intent(activity, CraftActivity::class.java)
            startActivity(intent)
        }
    }


//    MENDAPATKAN HASIL DARI CAMERAX
    private val launcherIntentCameraX =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
                currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
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

    val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {uri ->
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
                binding.ivDetect.setImageURI(it)
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}