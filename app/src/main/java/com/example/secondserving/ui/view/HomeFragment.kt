package com.example.secondserving.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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
import com.example.secondserving.data.Inventory
import com.example.secondserving.databinding.FragmentHomeBinding
import com.example.secondserving.utils.exhaustive
import com.example.secondserving.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), InventoryAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getUser()
        registerObservers()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        val inventoryAdapter = InventoryAdapter(this)
        //setHasOptionsMenu(true)

        binding.apply {
            recyclerViewInventory.apply {
                adapter = inventoryAdapter
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
                    val task = inventoryAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onInventorySwiped(task)
                }

            }).attachToRecyclerView(recyclerViewInventory)

            fabAddInventory.setOnClickListener {
                viewModel.onAddNewInventoryClick()
            }

        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        binding.fabAddInventory.setOnClickListener {
           viewModel.onAddNewInventoryClick()
        }

        inventoryAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                binding.recyclerViewInventory.scrollToPosition(0)
            }
        })

        viewModel.inventories.observe(viewLifecycleOwner) { //added observer to livedata
            inventoryAdapter.submitList(it)
            if (viewModel.inventories.value.isNullOrEmpty())
                binding.noInventory.visibility = View.VISIBLE
            else
                binding.noInventory.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.inventoryEvent.collect() { event ->
                when (event) {
                    HomeViewModel.InventoryEvent.NavigateToAddInventoryScreen -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToAddInventoryFragment(
                            "Add Inventory"
                        )
                        findNavController().navigate(action)
                    }

                    is HomeViewModel.InventoryEvent.NavigateToInventoryScreen -> {
                        val action = HomeFragmentDirections.actionHomeFragmentToInventoryFragment(
                            title = "Ingredients", inventory = event.inventory
                        )
                        findNavController().navigate(action)
                    }

                    is HomeViewModel.InventoryEvent.ShowInventorySavedConfirmation -> {
                        Snackbar.make(view, event.message, Snackbar.LENGTH_SHORT).show()
                    }

                    is HomeViewModel.InventoryEvent.ShowUndoDeleteInventoryMessage -> {
                        Snackbar.make(requireView(), "Inventory deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.inventory)
                            }.show()
                    }

                    HomeViewModel.InventoryEvent.NavigateToDeleteAllCompletedScreen -> TODO()
                }.exhaustive
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_nav_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_name -> {
         //       viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }

            R.id.sort_by_date_created -> {
           //     viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }

            R.id.hide_completed_tasks -> {
                item.isChecked = !item.isChecked
           //     viewModel.onHideCompletedClick(item.isChecked)
                true
            }

            R.id.delete_all_completed_tasks -> {
            //    viewModel.onDeleteAllCompleted()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(inventory: Inventory) {
        viewModel.onInventorySelected(inventory)
        Log.d("InventoryClick", "Inventory Item clicked ${inventory.name}")
    }

    private fun getUser() {
        viewModel.getCurrentUser()
    }

    private fun registerObservers() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewModel.init()
            } ?: startActivity(Intent(requireContext(), MainActivity::class.java))

        }
    }

}