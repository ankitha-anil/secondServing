package com.example.secondserving.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondserving.ADD_INVENTORY_RESULT_OK
import com.example.secondserving.EDIT_INVENTORY_RESULT_OK
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.IngredientDAO
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddInventoryViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val inventoryDAO: InventoryDAO,
    private val ingredientDAO: IngredientDAO,
    private val state: SavedStateHandle
): ViewModel() {

    val inventory = state.get<Inventory>("inventory")

    private val firebaseUser = MutableLiveData<FirebaseUser?>()
    val currentUser get() = firebaseUser
    var inventoryName = state.get<Inventory>("inventoryName")?: inventory?.name ?: ""
        set(value){ //setter function
            field = value
            state["inventoryName"] = value
        }
    

    private val addEditChannel = Channel<AddEditInventoryEvent>()
    val addEditTaskEvent = addEditChannel.receiveAsFlow()

    fun onSaveClick(){
        if(inventoryName.toString().isBlank()){
            showInvalidInputMessage("Name cannot be empty")
            return
        }
        if (inventory != null) {
            val updatedInventory = inventory.copy(name = inventoryName.toString())
            updateInventory(updatedInventory)
        } else {
            currentUser.value?.let {
                val newInventory = Inventory(name = inventoryName.toString(), userID = it.uid)
                createInventory(newInventory)
            }
        }
    }

    fun updateInventory(inventory: Inventory) = viewModelScope.launch {
        inventoryDAO.updateInventory(inventory)
        navigateWithMessage(EDIT_INVENTORY_RESULT_OK, inventoryName = inventory.name)
    }

    fun createInventory(inventory: Inventory) = viewModelScope.launch {
        inventoryDAO.insertInventory(inventory)
        navigateWithMessage(ADD_INVENTORY_RESULT_OK, inventoryName = inventory.name)
    }

    fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        addEditChannel.send(AddEditInventoryEvent.ShowInvalidInputMessage(message))
    }

    fun navigateWithMessage(result: Int, inventoryName: String) = viewModelScope.launch {
        addEditChannel.send(AddEditInventoryEvent.NavigateBackWithResult(result, inventoryName))
    }

    fun getCurrentUser() = viewModelScope.launch {
        firebaseUser.postValue(repository.getCurrentUser())
    }


    sealed class AddEditInventoryEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditInventoryEvent()
        data class NavigateBackWithResult(val result: Int, val inventoryName: String): AddEditInventoryEvent()
    }
}