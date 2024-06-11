package com.capstone.plasticwise.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.capstone.plasticwise.R
import com.capstone.plasticwise.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding

    private var dataRequest: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
            val transaction = supportFragmentManager
            val loginFragment = LoginFragment()
            val fragment = transaction.findFragmentByTag(LoginFragment::class.java.simpleName)
            if (fragment !is LoginFragment) {
                Log.d("My LoginFragment", "Fragment Name :" + LoginFragment::class.java.simpleName)
                transaction
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        loginFragment,
                        LoginFragment::class.java.simpleName
                    )
                    .commit()
            }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
}