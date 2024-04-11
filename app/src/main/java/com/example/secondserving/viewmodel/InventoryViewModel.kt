package com.example.secondserving.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.secondserving.ADD_INVENTORY_RESULT_OK
import com.example.secondserving.EDIT_INVENTORY_RESULT_OK
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.Ingredient
import com.example.secondserving.data.IngredientDAO
import com.example.secondserving.data.InvLineItemDisplay
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import com.example.secondserving.data.InventoryLineItem
import com.example.secondserving.data.InventoryLineItemDAO
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.toList
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

    var inventoryLineItemsDisplay = MutableLiveData<List<InvLineItemDisplay>>()


    //inventoryDAO.getInventoriesForUser("fSiLGeQcGDdVKHvH49jkqsGYsMz2").asLiveData()

    fun init() {
        viewModelScope.launch {
            inventoryLineItemDAO.getAllIngredientsByInventoryID(inventoryID = inventory?.id ?: 1)
                .collect { inventoryLineItems ->
                    inventoryLineItemsDisplay.postValue(inventoryLineItems)
                }
        }
    }

    fun onUndoDeleteClick(inventoryLineItem: InventoryLineItem) = viewModelScope.launch() {
        inventoryLineItemDAO.insertInventoryLineItem(inventoryLineItem)
    }

    fun onInventoryLineItemSelected(inventoryLineItemDisplay: InvLineItemDisplay) {
        viewModelScope.launch {
            val inventoryLineItem = inventoryLineItemDisplay.toInventoryLineItem()
            val inventory = inventoryDAO.getInventoryById(inventoryLineItem.inventoryID.toString()).first()
            inventoryEventChannel.send(
                InventoryLineItemEvent.NavigateToEditInvLineItemScreen(
                    inventoryLineItem, inventory
                )
            )
        }
    }

    fun onInventoryLineItemSwiped(invLineItemDisplay: InvLineItemDisplay) {
        val inventoryLineItem: InventoryLineItem = invLineItemDisplay.toInventoryLineItem()

        viewModelScope.launch {
            inventoryLineItemDAO.deleteInventoryLineItem(inventoryLineItem)
            inventoryEventChannel.send(
                InventoryLineItemEvent.ShowUndoDeleteIngredientMessage(
                    inventoryLineItem
                )
            )
        }
    }

    fun onAddNewInventoryLineItemClick(inventory: Inventory) = viewModelScope.launch {
        inventoryEventChannel.send(InventoryLineItemEvent.NavigateToAddInvLineItemScreen(inventory = inventory))
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

     fun onEditInventory(inventory: Inventory) = viewModelScope.launch {
        inventoryEventChannel.send(InventoryLineItemEvent.NavigateToEditInventoryScreen(inventory = inventory))
    }


    sealed class InventoryLineItemEvent {  //different variation, can later get warning when the when statement is not exhaustive, there are no other kinds of task events compiler know

        data class NavigateToAddInvLineItemScreen(val inventory: Inventory) : InventoryLineItemEvent()
        data class NavigateToEditInvLineItemScreen(val inventoryLineItem: InventoryLineItem, val inventory: Inventory) :
            InventoryLineItemEvent()

        data class ShowUndoDeleteIngredientMessage(val inventoryLineItem: InventoryLineItem) :
            InventoryLineItemEvent() // generic name cause viewmodel not sure of the view

        data class ShowInventorySavedConfirmation(val message: String) : InventoryLineItemEvent()
        object NavigateToDeleteAllCompletedScreen : InventoryLineItemEvent()
        data class NavigateToEditInventoryScreen(val inventory: Inventory) : InventoryLineItemEvent()

        object NavigateBackWithResult : InventoryViewModel.InventoryLineItemEvent()

    }
}