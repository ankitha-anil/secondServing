package com.example.secondserving.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.secondserving.R
import com.example.secondserving.databinding.FragmentAddInventoryBinding
import com.example.secondserving.utils.exhaustive
import com.example.secondserving.viewmodel.AddInventoryViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddInventoryFragment : Fragment(R.layout.fragment_add_inventory) {
    private val viewModel: AddInventoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getUser()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddInventoryBinding.bind(view)
        binding.apply {
            titleEt.setText(viewModel.inventoryName.toString())
            titleEt.addTextChangedListener {
                viewModel.inventoryName = it.toString()
            }

            addInventoryBtn.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddInventoryViewModel.AddEditInventoryEvent.NavigateBackWithResult -> {
                        binding.titleEt.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("updated_inventory_name", event.inventoryName)
                        findNavController().popBackStack()
                    }

                    is AddInventoryViewModel.AddEditInventoryEvent.ShowInvalidInputMessage ->
                        Snackbar.make(view, event.msg, Snackbar.LENGTH_LONG).show()
                }.exhaustive
            }
        }
    }


    fun getUser() {
        viewModel.getCurrentUser()
    }

}