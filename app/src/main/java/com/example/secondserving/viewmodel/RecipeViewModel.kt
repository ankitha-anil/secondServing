package com.example.secondserving.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.Recipe
import com.example.secondserving.data.RecipeDAO
import com.example.secondserving.data.RecipeRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val recipeDAO: RecipeDAO,
    private val recipeRepository: RecipeRepository

): ViewModel() {
    //val recipe = state.get<Recipe>("recipe")

    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser
/*
    var recipeName = state.get<Recipe>("recipeName")?: recipe?.recipeName ?: ""
        set(value){ //setter function
            field = value
            state["recipeName"] = value
        }
*/
    init {
        loadRecipesFromCsv()
    }
    private fun loadRecipesFromCsv() {
        val recipes = recipeRepository.readRecipesFromCsv()
        // Now you have a list of Recipe objects to work with
    }

    fun getCurrentUser() = viewModelScope.launch {
        firebaseUser.postValue(repository.getCurrentUser())
    }

    fun insertRecipe(recipe: Recipe) = viewModelScope.launch {
        recipeDAO.insertRecipe(recipe)
    }

}