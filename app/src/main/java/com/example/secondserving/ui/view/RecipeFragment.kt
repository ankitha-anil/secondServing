package com.example.secondserving.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.secondserving.R
import com.example.secondserving.databinding.FragmentProfileBinding
import com.example.secondserving.databinding.FragmentRecipeBinding
import com.example.secondserving.viewmodel.AuthViewModel
import com.example.secondserving.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.fragment_recipe) {
    private lateinit var binding: FragmentRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

}