package com.example.secondserving


import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.secondserving.databinding.HomeActivityBinding
import com.example.secondserving.viewmodel.AuthViewModel
import com.example.secondserving.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.secondserving.data.RecipeRepository


class RecipeActivity : AppCompatActivity() {
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        registerObservers()
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        // viewModel.loadRecipesFromCsv()
        // Observe ViewModel's LiveData and update UI accordingly

    }


    private fun registerObservers() {
        viewModel.currentUser.observe(this) { user ->
            user?.let {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }
}