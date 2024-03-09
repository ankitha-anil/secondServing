package com.example.secondserving.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.secondserving.R
import com.example.secondserving.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeleteUserDialog: DialogFragment() {
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_delete_user, null)
        val userPassword = view.findViewById<EditText>(R.id.passwordEt)
        view.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            viewModel.deleteUserandSignOut(userPassword.text.toString())
            dialog?.dismiss()
        }
        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog?.cancel()
        }

        return builder.create()
    }
}