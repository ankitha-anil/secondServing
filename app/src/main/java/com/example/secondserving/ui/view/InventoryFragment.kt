package com.example.secondserving.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondserving.MainActivity
import com.example.secondserving.R
import com.example.secondserving.data.InvLineItemDisplay
import com.example.secondserving.databinding.FragmentInventoryBinding
import com.example.secondserving.utils.exhaustive
import com.example.secondserving.viewmodel.InventoryViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InventoryFragment : Fragment(R.layout.fragment_inventory),
    InventoryLineItemAdapter.OnItemClickListener {
    private lateinit var binding: FragmentInventoryBinding

    private val viewModel: InventoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getUser()
        registerObservers()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInventoryBinding.bind(view)
        val inventoryLineItemAdapter = InventoryLineItemAdapter(this)
        setHasOptionsMenu(true)

        binding.apply {
            recyclerViewInventoryLineItem.apply {
                adapter = inventoryLineItemAdapter
                layoutManager = LinearLayoutManager(requireContext())
                root.apply {

                }
                setHasFixedSize(true)
            }
            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val invlineitem = inventoryLineItemAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onInventoryLineItemSwiped(invlineitem)
                }

            }).attachToRecyclerView(recyclerViewInventoryLineItem)

            fabAddIngredient.setOnClickListener {
                viewModel.inventory?.let { it1 -> viewModel.onAddNewInventoryLineItemClick(inventory = it1) }
            }

        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        inventoryLineItemAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                binding.recyclerViewInventoryLineItem.scrollToPosition(0)
            }
        })

        viewModel.inventoryLineItemsDisplay.observe(viewLifecycleOwner) { //added observer to livedata
            inventoryLineItemAdapter.submitList(it)
            if (viewModel.inventoryLineItemsDisplay.value.isNullOrEmpty())
                binding.noIngredients.visibility = View.VISIBLE
            else
                binding.noIngredients.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.inventoryEvent.collect() { event ->
                when (event) {
                   is InventoryViewModel.InventoryLineItemEvent.NavigateToAddIngredientScreen -> {
                        val action =
                            InventoryFragmentDirections.actionInventoryFragmentToAddInvLineItemFragment(
                                "Add Ingredient",
                                inventory = event.inventory
                            )
                        findNavController().navigate(action)
                    }

                    is InventoryViewModel.InventoryLineItemEvent.NavigateToEditIngredientScreen -> {
//                        val action = HomeFragmentDirections.actionHomeFragmentToInventoryFragment(
//                            "Ingredients", event.inventoryLineItem
//                        )
//                        findNavController().navigate(action)
                    }

                    is InventoryViewModel.InventoryLineItemEvent.ShowInventorySavedConfirmation -> {
                        Snackbar.make(view, event.message, Snackbar.LENGTH_SHORT).show()
                    }

                    is InventoryViewModel.InventoryLineItemEvent.ShowUndoDeleteIngrdientMessage -> {
//                        Snackbar.make(requireView(), "Inventory deleted", Snackbar.LENGTH_LONG)
//                            .setAction("UNDO") {
//                                viewModel.onUndoDeleteClick(event.inventory)
//                            }.show()
                    }

                    InventoryViewModel.InventoryLineItemEvent.NavigateToDeleteAllCompletedScreen -> TODO()
                    InventoryViewModel.InventoryLineItemEvent.NavigateBackWithResult -> TODO()
                    else -> {}
                }.exhaustive
            }
        }

    }

    fun getUser() {
        viewModel.getCurrentUser()
    }

    private fun registerObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewModel.init()
            } ?: startActivity(Intent(requireContext(), MainActivity::class.java))

        }
    }

    override fun onItemClick(inventoryLineItem: InvLineItemDisplay) {
        TODO("Go to Ingredient details")
    }

}