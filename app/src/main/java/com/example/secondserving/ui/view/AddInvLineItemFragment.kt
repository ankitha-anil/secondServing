package com.example.secondserving.ui.view

import IngredientSpinnerAdapter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.secondserving.R
import com.example.secondserving.databinding.FragmentAddInvlineitemBinding
import com.example.secondserving.utils.exhaustive
import com.example.secondserving.viewmodel.AddInvLineItemViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddInvLineItemFragment : Fragment(R.layout.fragment_add_invlineitem) {
    private val viewModel: AddInvLineItemViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getUser()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddInvlineitemBinding.bind(view)
        binding.apply {
            inventoryName.setText(viewModel.inventoryName.toString())
            inventoryName.addTextChangedListener {
                viewModel.inventoryName = it.toString()
            }

            val ingredientsList: MutableList<Pair<Int, String>> = mutableListOf()
            val adapter = IngredientSpinnerAdapter(requireContext(), ingredientsList)
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            viewModel.ingredientNamesAndIds.observe(viewLifecycleOwner) { ingredients ->
                ingredientsList.clear()
                ingredientsList.addAll(ingredients)
                adapter.notifyDataSetChanged()
            }
            ingredientSpinner.adapter = adapter

            ingredientSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedIngredient = adapter.getItem(position)
                    viewModel.ingredientId = selectedIngredient?.first.toString().toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    val selectedIngredient = adapter.getItem(0)
                    viewModel.ingredientId = selectedIngredient?.first.toString().toInt()
                }
            }
            quantity.addTextChangedListener {
                viewModel.quantity = it.toString()
            }

            expiryDate.setOnDateChangedListener { datePicker, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val expiryInMillis = calendar.timeInMillis
                viewModel.expiry = expiryInMillis
            }

            btnSave.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddInvLineItemViewModel.AddEditInvLineItemEvent.NavigateBackWithResult -> {
                        binding.inventoryName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }

                    is AddInvLineItemViewModel.AddEditInvLineItemEvent.ShowInvalidInputMessage ->
                        Snackbar.make(view, event.msg, Snackbar.LENGTH_LONG).show()

                    is AddInvLineItemViewModel.AddEditInvLineItemEvent.ShowToastMessage ->
                        Toast.makeText(context, event.msg, Toast.LENGTH_LONG).show()

                    else -> {}
                }.exhaustive
            }
        }
    }


    fun getUser() {
        viewModel.getCurrentUser()
    }

}