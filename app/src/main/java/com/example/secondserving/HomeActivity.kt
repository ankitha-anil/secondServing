package com.example.secondserving

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.secondserving.databinding.HomeActivityBinding
import com.example.secondserving.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    lateinit var binding: HomeActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutbtn.setOnClickListener {
            viewModel.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}