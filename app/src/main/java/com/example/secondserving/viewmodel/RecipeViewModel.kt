package com.example.secondserving.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.Recipe
import com.example.secondserving.data.RecipeDAO
import com.example.secondserving.data.RecipeRepository
import com.example.secondserving.ui.view.RecipeFragment
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val recipeDAO: RecipeDAO,
    private val recipeRepository: RecipeRepository

): ViewModel() {

    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser
    private val recipeEventChannel = Channel<RecipeViewModel.RecipeEvent>()
    val recipeEvent = recipeEventChannel.receiveAsFlow() // receive as flow

    var recipes = MutableLiveData<List<Recipe>>()

    fun init() {
        //loadRecipesFromCsv()
        viewModelScope.launch {
            recipeDAO.findRecipesWithIngredient("Apple")
                .collect { recipe ->
                    recipes.postValue(recipe)
                }
        }
    }
    fun loadRecipesFromCsv() {
        // This should be executed in a background thread
        val recipesList = recipeRepository.readRecipesFromCsv()
        recipes.value = recipesList
    }

    fun getCurrentUser() = viewModelScope.launch {
        firebaseUser.postValue(repository.getCurrentUser())
    }

    fun onRecipeSelected(recipe: Recipe){
        viewModelScope.launch {
            recipeEventChannel.send(RecipeEvent.NavigateToRecipeDetailScreen(recipe))
        }
    }

    fun insertRecipe(recipe: Recipe) = viewModelScope.launch {
        recipeDAO.insertRecipe(recipe)
    }

    sealed class RecipeEvent {  //different variation, can later get warning when the when statement is not exhaustive, there are no other kinds of task events compiler know
        data class NavigateToRecipeDetailScreen(val recipe: Recipe) : RecipeEvent()
    }

}