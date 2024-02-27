package com.example.secondserving.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.secondserving.R
import com.example.secondserving.databinding.FragmentHomeBinding
import com.example.secondserving.databinding.FragmentProfileBinding
import com.example.secondserving.databinding.FragmentRecipeBinding
import com.example.secondserving.viewmodel.AuthViewModel
import com.example.secondserving.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        getUser()
        return binding.root
    }
    private fun getUser() {
        viewModel.getCurrentUser()
    }

}