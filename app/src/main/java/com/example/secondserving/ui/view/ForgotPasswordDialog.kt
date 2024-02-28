package com.example.secondserving.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.secondserving.R
import com.example.secondserving.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordDialog : DialogFragment() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.dialog_forgot, null, false);
        val userEmail = view.findViewById<EditText>(R.id.editBox)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        view.findViewById<Button>(R.id.btnReset).setOnClickListener {
            viewModel.changePassword(userEmail.text.toString())
            dialog?.dismiss()
        }
        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog?.cancel()
        }
        return view;

    }
}