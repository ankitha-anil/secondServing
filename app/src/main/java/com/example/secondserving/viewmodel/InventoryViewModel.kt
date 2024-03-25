package com.example.secondserving.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.secondserving.ADD_INVENTORY_RESULT_OK
import com.example.secondserving.EDIT_INVENTORY_RESULT_OK
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.Ingredient
import com.example.secondserving.data.IngredientDAO
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import com.example.secondserving.data.InventoryLineItem
import com.example.secondserving.data.InventoryLineItemDAO
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val inventoryDAO: InventoryDAO,
    private val ingredientDAO: IngredientDAO,
    private val inventoryLineItemDAO: InventoryLineItemDAO,
    private val state: SavedStateHandle
) : ViewModel() {

    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser
    val inventory = state.get<Inventory>("inventory")

    private val inventoryEventChannel = Channel<InventoryLineItemEvent>()
    private val inventoryLineItemEventChannel = Channel<InventoryLineItemEvent>()

    val inventoryEvent = inventoryEventChannel.receiveAsFlow() // receive as flow
    val inventoryLineItemEvent = inventoryLineItemEventChannel.receiveAsFlow() // receive as flow

    var inventories = MutableLiveData<List<Inventory>>()
    var ingredients = MutableLiveData<List<Ingredient>>()
    var inventoryLineItems = MutableLiveData<List<InventoryLineItem>>()

    //inventoryDAO.getInventoriesForUser("fSiLGeQcGDdVKHvH49jkqsGYsMz2").asLiveData()

    fun init() {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                inventoryLineItemDAO.getAllInventoryLineItemsByInventoryAndUserID(
                    inventoryID = inventory?.id ?: 1,
                    userID = user.uid
                )
            }?.collect { inventoryLineItem ->
                inventoryLineItems.postValue(inventoryLineItem)
            }
//            inventoryLineItems.map {
//                it.forEach {
//                    val ingredient = ingredientDAO.getIngredientById(it.ingredientID).first()
//                }.collect { ingredient ->
//                    ingredients.postValue(ingredient)
//                }
//            }
        }
    }

    fun onUndoDeleteClick(inventoryLineItem: InventoryLineItem) {}

    fun onInventoryLineItemSelected(inventoryLineItem: InventoryLineItem) {
        viewModelScope.launch {
            inventoryEventChannel.send(
                InventoryLineItemEvent.NavigateToEditIngredientScreen(
                    inventoryLineItem
                )
            )
        }
    }

    fun onInventoryLineItemSwiped(inventoryLineItem: InventoryLineItem) = viewModelScope.launch {
        inventoryLineItemDAO.deleteInventoryLineItem(inventoryLineItem)
        inventoryEventChannel.send(
            InventoryLineItemEvent.ShowUndoDeleteIngrdientMessage(
                inventoryLineItem
            )
        )
    }

    fun onAddNewInventoryLineItemClick( inventory: Inventory) = viewModelScope.launch {
        inventoryEventChannel.send(InventoryLineItemEvent.NavigateToAddIngredientScreen(inventory = inventory))
    }


    fun getCurrentUser() = viewModelScope.launch {
        firebaseUser.postValue(repository.getCurrentUser())
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_INVENTORY_RESULT_OK -> InventoryLineItemEvent.ShowInventorySavedConfirmation("Inventory Added")
            EDIT_INVENTORY_RESULT_OK -> InventoryLineItemEvent.ShowInventorySavedConfirmation("Inventory Updated")
        }
    }


    sealed class InventoryLineItemEvent {  //different variation, can later get warning when the when statement is not exhaustive, there are no other kinds of task events compiler know
        data class NavigateToAddIngredientScreen(val inventory: Inventory) : InventoryLineItemEvent()
        data class NavigateToEditIngredientScreen(val inventoryLineItem: InventoryLineItem) :
            InventoryLineItemEvent()

        data class ShowUndoDeleteIngrdientMessage(val inventoryLineItem: InventoryLineItem) :
            InventoryLineItemEvent() // generic name cause viewmodel not sure of the view

        data class ShowInventorySavedConfirmation(val message: String) : InventoryLineItemEvent()
        object NavigateToDeleteAllCompletedScreen : InventoryLineItemEvent()

        object NavigateBackWithResult : InventoryViewModel.InventoryLineItemEvent()

    }
}