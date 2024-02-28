package com.example.secondserving.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.secondserving.HomeActivity
import com.example.secondserving.R
import com.example.secondserving.databinding.SignupFragmentBinding
import com.example.secondserving.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.signup_fragment) {

    private val viewModel: AuthViewModel by viewModels()
    private var binding: SignupFragmentBinding? = null

    var TAG = "SignUpFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SignupFragmentBinding.inflate(inflater, container, false)
        registerObservers()
        listenToChannels()
        binding?.apply {
            emailEt.setText("ankitha.anil@gmail.com")
            PassEt.setText("123456")
            confPassEt.setText("123456")
            signButton.setOnClickListener {
                val email = emailEt.text.toString()
                val password = PassEt.text.toString()
                val confirmPass = confPassEt.text.toString()

                viewModel.signUpUser(email = email, password = password, confirmPass = confirmPass)
            }

            redirectLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
        return binding?.root
    }

    private fun registerObservers() {
        viewModel.currentUser.observe(requireActivity()) { user ->
            user?.let {
                startActivity(Intent(requireContext(), HomeActivity::class.java))
            }
        }
    }

    private fun listenToChannels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allEventsFlow.collect { event ->
                when (event) {
                    is AuthViewModel.AllEvents.Error -> {
                        binding?.apply {
                            Toast.makeText(requireContext(), event.error, Toast.LENGTH_SHORT).show()
                        }
                    }

                    is AuthViewModel.AllEvents.Message -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }

                    is AuthViewModel.AllEvents.ErrorCode -> {
                        if (event.code == 1)
                            binding?.apply {
                                emailEt.setError("Email should not be empty")
                                emailEt.requestFocus()
                                emailEt.error = "Email should not be empty"
                            }

                        if (event.code == 2)
                            binding?.apply {
                                PassEt.error = "Password should not be empty"
                            }

                        if (event.code == 3)
                            binding?.apply {
                                confPassEt.error = "Passwords do not match"
                            }
                    }

                    else -> {
                        Log.d(TAG, "listenToChannels: No event received so far")
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}