package com.example.secondserving.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.secondserving.ADD_INVENTORY_RESULT_OK
import com.example.secondserving.ADD_INVLINEITEM_RESULT_OK
import com.example.secondserving.EDIT_INVENTORY_RESULT_OK
import com.example.secondserving.EDIT_INVLINEITEM_RESULT_OK
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.IngredientDAO
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import com.example.secondserving.data.InventoryLineItem
import com.example.secondserving.data.InventoryLineItemDAO
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddInvLineItemViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val inventoryDAO: InventoryDAO,
    private val inventoryLineItemDAO: InventoryLineItemDAO,
    private val ingredientDAO: IngredientDAO,
    private val state: SavedStateHandle
) : ViewModel() {

    val inventory = state.get<Inventory>("inventory")
    val invlineitem = state.get<InventoryLineItem>("invlineitem")


    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser


    // Create an empty list to hold ingredient names and IDs
    val ingredientsList: MutableList<Pair<Int, String>> = mutableListOf()
    val ingredientNamesAndIds = ingredientDAO.getIngredientNamesAndIds()
    .map { list ->
        list.map { it.ingredientID to it.name } // Correcting the pair order
    }
    .asLiveData()
    var inventoryName = state.get<Inventory>("inventoryName") ?: inventory?.name ?: ""
        set(value) { //setter function
            field = value
            state["inventoryName"] = value
        }

    var inventoryID = state.get<Inventory>("inventoryID") ?: inventory?.id ?: ""
        set(value) { //setter function
            field = value
            state["inventoryID"] = value
        }
    var ingredientId = 0 //TODO: Get this proper id from dropdown

    var expiry = state.get<InventoryLineItem>("expiryDate") ?: invlineitem?.expiryDate ?: ""
        set(value) { //setter function
            field = value
            state["expiryDate"] = value
        }

    var quantity = state.get<InventoryLineItem>("quantity") ?: invlineitem?.quantity ?: ""
        set(value) { //setter function
            field = value
            state["quantity"] = value
        }


    private val addEditChannel = Channel<AddEditInvLineItemEvent>()
    val addEditTaskEvent = addEditChannel.receiveAsFlow()

    fun onSaveClick() {
//        if (inventoryName.toString().isBlank()) {
//            showInvalidInputMessage("Name cannot be empty")
//            return
//        }
        if (invlineitem != null) {
            val updatedInvLineItem = invlineitem.copy(
                inventoryID = inventoryID.toString().toInt(),
                ingredientID = ingredientId.toString().toInt(),
                expiryDate = expiry.toString().toLong(),
                quantity = quantity.toString().toInt(),
            )
            updateInvLineItem(updatedInvLineItem)
        } else {
            currentUser.value?.let {
                val newInvLineItem = InventoryLineItem(
                    inventoryID = inventoryID.toString().toInt(),
                    ingredientID = ingredientId.toString().toInt(),
                    expiryDate = expiry.toString().toLong(),
                    quantity = quantity.toString().toInt(),
                )
                createInvLineItem(newInvLineItem)
            }
        }
    }

    fun updateInvLineItem(inventoryLineItem: InventoryLineItem) = viewModelScope.launch {
        inventoryLineItemDAO.updateInventoryLineItem(inventoryLineItem)
        navigateWithMessage(EDIT_INVLINEITEM_RESULT_OK)
    }

    fun createInvLineItem(inventoryLineItem: InventoryLineItem) {
        viewModelScope.launch {
            inventoryLineItemDAO.insertInventoryLineItem(inventoryLineItem)
            navigateWithMessage(ADD_INVLINEITEM_RESULT_OK)
        }
    }

    fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        addEditChannel.send(AddEditInvLineItemEvent.ShowInvalidInputMessage(message))
    }

    fun navigateWithMessage(result: Int) = viewModelScope.launch {
        addEditChannel.send(AddEditInvLineItemEvent.NavigateBackWithResult(result))
    }

    fun getCurrentUser() = viewModelScope.launch {
        firebaseUser.postValue(repository.getCurrentUser())
    }

    fun populateIngredientList() {
//TODO: popoulate the ingredients from db
    }


    sealed class AddEditInvLineItemEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditInvLineItemEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditInvLineItemEvent()
    }
}