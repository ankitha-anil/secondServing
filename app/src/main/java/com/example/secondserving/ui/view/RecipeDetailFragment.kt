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
import com.example.secondserving.data.Recipe
import com.example.secondserving.databinding.FragmentInventoryBinding
import com.example.secondserving.databinding.FragmentRecipeDetailBinding
import com.example.secondserving.utils.exhaustive
import com.example.secondserving.viewmodel.InventoryViewModel
import com.example.secondserving.viewmodel.RecipeDetailViewModel
import com.example.secondserving.viewmodel.RecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail)
{
    private lateinit var binding: FragmentRecipeDetailBinding
    private val viewModel: RecipeDetailViewModel by viewModels()

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
        binding = FragmentRecipeDetailBinding.bind(view)

        binding.apply {

            // Recipe Name
            textRecipeName.text = viewModel.recipe?.recipeName?.replace("\"", "")

            // Recipe Description
            textRecipeDescription.text = viewModel.recipe?.recipeDescription?.replace("\"", "")

            // Recipe Ingredients
            viewModel.recipe?.recipeIngredients?.let { ingredients ->
                val formattedIngredients = ingredients.replace("-", "\n").replace("\"", "")
                textIngredientsList.text = formattedIngredients
            }

            // Recipe Steps
            viewModel.recipe?.recipeSteps?.let {  steps ->
                val formattedSteps = StringBuilder()
                val stepPattern = "(\\d+\\.\\s*)(.*?)(?=(\\d+\\.|$))"
                val stepMatcher = Regex(stepPattern).findAll(steps)
                stepMatcher.forEach { matchResult ->
                    val step = matchResult.groupValues[2]
                    formattedSteps.append("${matchResult.groupValues[1]}$step\n")
                }
                textRecipeStepsList.text = formattedSteps.toString().replace("\"", "")

            }
        }



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipeEvent.collect() { event ->
                when (event) {


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
//                viewModel.init()
            } ?: startActivity(Intent(requireContext(), MainActivity::class.java))

        }
    }

}