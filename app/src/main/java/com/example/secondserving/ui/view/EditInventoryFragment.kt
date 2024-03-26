package com.example.secondserving.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.secondserving.data.Inventory
import com.example.secondserving.databinding.EditInventoryBinding
import com.example.secondserving.viewmodel.AddInventoryViewModel

class EditInventoryFragment : Fragment() {

    private var _binding: EditInventoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddInventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentInventory = arguments?.getParcelable<Inventory>("inventory")

        currentInventory?.let { inventory ->
            // Set inventory data to ViewModel
            viewModel.setCurrentInventory(inventory)
            // Update UI with inventory data
            binding.titleEt.setText(inventory.name)
        }

        binding.editProductBtn.setOnClickListener {
            val newName = binding.titleEt.text.toString().trim()
            if (newName.isNotEmpty()) {
                viewModel.onSaveClick()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
