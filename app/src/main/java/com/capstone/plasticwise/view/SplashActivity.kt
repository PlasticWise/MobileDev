package com.capstone.plasticwise

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstone.plasticwise.databinding.ActivitySplashBinding
import com.capstone.plasticwise.view.HomeActivity
import com.capstone.plasticwise.view.WelcomeActivity
import com.capstone.plasticwise.viewModel.SplashScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashScreenViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        auth = FirebaseAuth.getInstance()

        binding.iconPicture.startAnimation(fadeInAnimation)
        binding.splashText.startAnimation(fadeInAnimation)

        observeSession()
    }

    private fun observeSession() {
        lifecycleScope.launch {
            delay(2500)
            val user = auth.currentUser
            if (user != null) {
                // Log to help debug
                user.getIdToken(true).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("User is already logged in, navigating to HomeActivity")
                        navigateToHomeActivity()
                    } else {
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException || exception is FirebaseAuthInvalidCredentialsException) {
                            navigateToUserActivity()
                        } else {
                            exception?.printStackTrace()
                            navigateToUserActivity()
                        }
                    }
                }
            } else {
                // Log to help debug
                println("No user logged in, navigating to UserActivity")
                navigateToUserActivity()
            }
        }
    }

    private fun navigateToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun navigateToUserActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}
