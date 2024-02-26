package com.example.secondserving.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.secondserving.R
import com.example.secondserving.databinding.FragmentHomeBinding
import com.example.secondserving.databinding.FragmentProfileBinding
import com.example.secondserving.databinding.FragmentRecipeBinding
import com.example.secondserving.viewmodel.AuthViewModel
import com.example.secondserving.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.secondserving.AddInventoryActivity

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the click listener for the FAB
        binding.fabAdd.setOnClickListener {
            // Use context to create an Intent because we're in a Fragment
            val intent = Intent(context, AddInventoryActivity::class.java)
            startActivity(intent)
        }
    }
}