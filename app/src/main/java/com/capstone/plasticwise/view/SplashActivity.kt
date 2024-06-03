package com.capstone.plasticwise

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.plasticwise.databinding.ActivitySplashBinding
import com.capstone.plasticwise.view.HomeActivity
import com.capstone.plasticwise.view.UserActivity
import com.capstone.plasticwise.viewModel.SplashScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)


        binding.iconPicture.startAnimation(fadeInAnimation)
        binding.splashText.startAnimation(slideUpAnimation)

        observeSession()
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { session ->
            lifecycleScope.launch {
                delay(3000) // 3 second delay
                if(!session.isLogin) {
                    navigateToUserActivity()
                } else {
                    navigateToHomeActivity()
                }
            }
        }
    }
    private fun navigateToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
    private fun navigateToUserActivity() {
        startActivity(Intent(this, UserActivity::class.java))
        finish()
    }
}
