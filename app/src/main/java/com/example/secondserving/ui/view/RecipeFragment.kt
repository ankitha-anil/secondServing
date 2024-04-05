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
import com.example.secondserving.databinding.FragmentRecipeBinding
import com.example.secondserving.utils.exhaustive
import com.example.secondserving.viewmodel.AddInventoryViewModel
import com.example.secondserving.viewmodel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.fragment_recipe) {
    private val viewModel: RecipeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRecipeBinding.bind(view)
        val recipeAdapter = RecipeAdapter()
        binding.recyclerViewRecipes.adapter = recipeAdapter

        // Assuming your ViewModel has a LiveData or Flow that emits a list of recipes
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            // Update the adapter with new recipe list
            recipeAdapter.submitList(recipes)
        }

        viewModel.loadRecipesFromCsv() // USE THIS!! DONT USE ACTIVITIES USE FRAGMENTS ***
        // USE ADAPTER :)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        }
    }



    fun getUser() {
        viewModel.getCurrentUser()
    }

}