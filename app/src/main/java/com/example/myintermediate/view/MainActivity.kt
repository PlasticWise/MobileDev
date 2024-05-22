package com.example.myintermediate.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myintermediate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        clickedButton()
    }

    private fun clickedButton() {
        binding.apply {
            btnLogin.setOnClickListener {
                val intent = Intent(this@MainActivity, UserActivity::class.java)
                intent.putExtra(CODE_FRAGMENT, 10)
                startActivity(intent)
            }

            btnSignup.setOnClickListener {
                val intent = Intent(this@MainActivity, UserActivity::class.java)
                intent.putExtra(CODE_FRAGMENT, 20)
                startActivity(intent)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0f, 1f).setDuration(1000)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 0f, 1f).setDuration(1900)
        val title = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 0F, 1F).setDuration(1000)
        val description =
            ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 0F, 1F).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, description, together)
            start()
        }
    }

    companion object {
        const val CODE_FRAGMENT = "10"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}