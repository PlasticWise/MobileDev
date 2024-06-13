package com.capstone.plasticwise.view

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.capstone.plasticwise.R
import com.capstone.plasticwise.databinding.ActivityCraftBinding

class CraftActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCraftBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCraftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        val btnCraft = findViewById<Button>(R.id.btnCraft)
        btnCraft.setOnClickListener {
            val detectFragment = DetectFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragDetect, detectFragment)
                .commit()
        }

//        binding.btnCraft.setOnClickListener{
//            findNavController().navigate(R.id.action_nav_craft_to_nav_detect)
//        }
    }
}
