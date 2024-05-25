package com.capstone.plasticwise

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.capstone.plasticwise.databinding.ActivitySplashBinding
import com.capstone.plasticwise.view.UserActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)


        binding.iconPicture.startAnimation(fadeInAnimation)
        binding.splashText.startAnimation(slideUpAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, UserActivity::class.java))
            finish()
        }, 3000) // 3 seconds delay
    }
}
