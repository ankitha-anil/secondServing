package com.example.secondserving.ui.view

import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondserving.MainActivity
import com.example.secondserving.R
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.Recipe
import com.example.secondserving.databinding.FragmentAddInventoryBinding
import com.example.secondserving.databinding.FragmentHomeBinding
import com.example.secondserving.databinding.FragmentRecipeBinding
import com.example.secondserving.utils.exhaustive
import com.example.secondserving.viewmodel.AddInventoryViewModel
import com.example.secondserving.viewmodel.HomeViewModel
import com.example.secondserving.viewmodel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.fragment_recipe), RecipeAdapter.OnItemClickListener {
    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var binding: FragmentRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getUser()
        registerObservers()
        binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRecipeBinding.bind(view)
        val recipeAdapter = RecipeAdapter(this)

        binding.apply {
            recyclerViewRecipes.apply{
                adapter = recipeAdapter
                layoutManager = LinearLayoutManager(requireContext())
                root.apply {

                }
                setHasFixedSize(true)
            }
        }

        // Assuming your ViewModel has a LiveData or Flow that emits a list of recipes
        viewModel.recipes.observe(viewLifecycleOwner) {
            // Update the adapter with new recipe list
            recipeAdapter.submitList(it)
            if (viewModel.recipes.value.isNullOrEmpty())
                binding.noRecipes.visibility = View.VISIBLE
            else
                binding.noRecipes.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipeEvent.collect() { event ->
                when (event) {
                    is RecipeViewModel.RecipeEvent.NavigateToRecipeDetailScreen -> {
                        val action = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailFragment(
                            title = event.recipe.recipeName+" Recipe", recipe = event.recipe
                        )
                        findNavController().navigate(action)
                    }
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

    override fun onItemClick(recipe: Recipe) {
        viewModel.onRecipeSelected(recipe)
    }


}