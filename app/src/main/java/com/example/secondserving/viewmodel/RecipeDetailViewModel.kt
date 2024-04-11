package com.example.secondserving.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondserving.ADD_INVENTORY_RESULT_OK
import com.example.secondserving.EDIT_INVENTORY_RESULT_OK
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.InvLineItemDisplay
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryLineItem
import com.example.secondserving.data.Recipe
import com.example.secondserving.data.RecipeDAO
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser
    val recipe = state.get<Recipe>("recipe")

     val recipeDetailEventChannel = Channel<RecipeDetailEvent>()

    val recipeEvent = recipeDetailEventChannel.receiveAsFlow() // receive as flow

    fun getCurrentUser() = viewModelScope.launch {
        firebaseUser.postValue(repository.getCurrentUser())
    }

    sealed class RecipeDetailEvent {}

}