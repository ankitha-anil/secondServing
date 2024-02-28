package com.example.secondserving.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.secondserving.R
import com.example.secondserving.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserNameDialog : DialogFragment() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_edit_username, null)
        val userName = view.findViewById<EditText>(R.id.usernameEt)
        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            viewModel.editUsername(userName.text.toString())
            dialog?.dismiss()
        }
        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog?.cancel()
        }

        return builder.create()
    }
}