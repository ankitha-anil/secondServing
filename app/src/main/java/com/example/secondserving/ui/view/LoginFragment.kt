package com.example.secondserving.ui.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.secondserving.HomeActivity
import com.example.secondserving.R
import com.example.secondserving.databinding.LoginFragmentBinding
import com.example.secondserving.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val viewModel: AuthViewModel by viewModels()
    private var binding: LoginFragmentBinding? = null
    private val TAG = "LogInFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        getUser()
        registerObservers()
        listenToChannels()
        binding?.apply {

            btnLogin.setOnClickListener {
                viewModel.signInUser(
                    email = emailEt.text.toString(),
                    password = PassEt.text.toString()
                )
            }

            signUpRedirect.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

            forgotPassword.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
                val userEmail = view.findViewById<EditText>(R.id.editBox)

                builder.setView(view)
                val dialog = builder.create()

                view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                    viewModel.compareEmail(userEmail.text.toString())
                    dialog.dismiss()
                }

                view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                    dialog.dismiss()
                }

                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0));
                }
                dialog.show()
                //findNavController().navigate(R.id.action_global_forgotPasswordDialog)
            }
        }
        return binding?.root
    }

    private fun getUser() {
        viewModel.getCurrentUser()
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
                            Toast.makeText(requireContext(), event.error, Toast.LENGTH_LONG).show()
                        }
                    }

                    is AuthViewModel.AllEvents.Message -> {
                        Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }

                    is AuthViewModel.AllEvents.ErrorCode -> {
                        if (event.code == 1)
                            binding?.apply {
                                emailEt.error = "Email should not be empty"
                            }

                        if (event.code == 2)
                            binding?.apply {
                                PassEt.error = "Password should not be empty"
                            }
                        if (event.code == 3)
                            binding?.apply {
                                PassEt.error = "Passwords do not match"
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