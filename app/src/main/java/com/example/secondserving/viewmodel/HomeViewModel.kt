package com.example.secondserving.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val inventoryDAO: InventoryDAO,
    private val ingredientDAO: IngredientDAO,
    private val inventoryLineItemDAO: InventoryLineItemDAO,
) : ViewModel() {

    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser

    private val inventoryEventChannel = Channel<InventoryEvent>()
    private val inventoryLineItemEventChannel = Channel<InventoryEvent>()

    val inventoryEvent = inventoryEventChannel.receiveAsFlow() // receive as flow
    val inventoryLineItemEvent = inventoryLineItemEventChannel.receiveAsFlow() // receive as flow

    var inventories = MutableLiveData<List<Inventory>>()
    var ingredients = MutableLiveData<List<Ingredient>>()
    var inventoryLineItems = MutableLiveData<List<InventoryLineItem>>()

    //inventoryDAO.getInventoriesForUser("fSiLGeQcGDdVKHvH49jkqsGYsMz2").asLiveData()

    fun init() {
        viewModelScope.launch {
            currentUser.value?.let { user ->
                inventoryDAO.getInventoriesForUser(
                    user.uid
                )
            }?.collect { inventoryItem ->
                inventories.postValue(inventoryItem)
            }
        }
    }

    fun onInventorySelected(inventory: Inventory) {
        viewModelScope.launch {
            inventoryEventChannel.send(InventoryEvent.NavigateToInventoryScreen(inventory))
        }
    }

    fun onInventorySwiped(inventory: Inventory) = viewModelScope.launch {
        inventoryDAO.deleteInventory(inventory)
    }

    fun onAddNewInventoryClick() = viewModelScope.launch {
        inventoryEventChannel.send(InventoryEvent.NavigateToAddInventoryScreen)
    }


    fun getCurrentUser() = viewModelScope.launch {
        firebaseUser.postValue(repository.getCurrentUser())
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_INVENTORY_RESULT_OK -> InventoryEvent.ShowInventorySavedConfirmation("Inventory Added")
            EDIT_INVENTORY_RESULT_OK -> InventoryEvent.ShowInventorySavedConfirmation("Inventory Updated")
        }
    }


    sealed class InventoryEvent {  //different variation, can later get warning when the when statement is not exhaustive, there are no other kinds of task events compiler know
        object NavigateToAddInventoryScreen : InventoryEvent()
        data class NavigateToInventoryScreen(val inventory: Inventory) : InventoryEvent()
        data class ShowUndoDeleteInventoryMessage(val inventory: Inventory) :
            InventoryEvent() // generic name cause viewmodel not sure of the view

        data class ShowInventorySavedConfirmation(val message: String) : InventoryEvent()
        object NavigateToDeleteAllCompletedScreen : InventoryEvent()
    }

}