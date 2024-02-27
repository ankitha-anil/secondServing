package com.example.secondserving.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.secondserving.MainActivity
import com.example.secondserving.R
import com.example.secondserving.databinding.FragmentProfileBinding
import com.example.secondserving.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        getUser()
        registerObserver()
        listenToChannels()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun registerObserver() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.apply {
                    email.text = it.email
                    changePassword.setOnClickListener {
                        viewModel.changePasswordAndSignOut()
                    }
                    editUsername.setOnClickListener {
                        viewModel.editUsername()
                    }
                    logoutbtn.setOnClickListener {
                        viewModel.signOut()
                    }
                    deleteProfile.setOnClickListener {
                        viewModel.deleteUserandSignOut("ankitha.anil@gmail.com", "123456")
                    }
                }
            }
        }
    }


    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allEventsFlow.collect { event ->
                when (event) {
                    is AuthViewModel.AllEvents.Error -> {
                        binding.apply {
                            Toast.makeText(requireContext(), event.error, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is AuthViewModel.AllEvents.Message -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }

                    is AuthViewModel.AllEvents.StartMainActivity -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }

                    else -> {
                        Log.d("Profile fragment", "listenToChannels: No event received so far")
                    }
                }

            }
        }
    }

    private fun getUser() {
        viewModel.getCurrentUser()
    }

}