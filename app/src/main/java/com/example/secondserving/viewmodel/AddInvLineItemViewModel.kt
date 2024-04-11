package com.example.secondserving.viewmodel

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.secondserving.ADD_INVLINEITEM_RESULT_OK
import com.example.secondserving.EDIT_INVLINEITEM_RESULT_OK
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.IngredientDAO
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryLineItem
import com.example.secondserving.data.InventoryLineItemDAO
import com.example.secondserving.utils.NotifyWork
import com.example.secondserving.utils.NotifyWork.Companion.NOTIFICATION_ID
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddInvLineItemViewModel @Inject constructor(
    private val repository: AuthRepository,
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
    var ingredientId =
        state.get<InventoryLineItem>("ingredientID") ?: invlineitem?.ingredientID ?: 0
        set(value) { //setter function
            field = value
            state["ingredientID"] = value
        }

    var expiry =
        state.get<InventoryLineItem>("expiryDate") ?: invlineitem?.expiryDate ?: currentTimeMillis()
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
        if (quantity.toString().isBlank()) {
            showInvalidInputMessage("Quantity cannot be empty")
            return
        }

        if (!quantity.toString().isDigitsOnly()) {
            showInvalidInputMessage("Quantity can only be numerical value")
            return
        }


        val todayCalendar = Calendar.getInstance()
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
        todayCalendar.set(Calendar.MINUTE, 0)
        todayCalendar.set(Calendar.SECOND, 0)
        todayCalendar.set(Calendar.MILLISECOND, 0)
        val todayMillis = todayCalendar.timeInMillis

        if (expiry.toString().toLong() < System.currentTimeMillis()) {
            showInvalidInputMessage("Expiry date cannot be before today")
            return
        } else if (expiry.toString().toLong() == todayMillis) {
            showInvalidInputMessage("Expiry date cannot be today")
            return
        }

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

        val customTime = expiry.toString().toLong()
        val currentTime = currentTimeMillis()
        val ingredient = getIngredientName(ingredientId.toString().toInt())

        val notificationText = "${ingredient.value} from $inventoryName is expiring today."
        if (customTime > currentTime) {
            val data = Data.Builder().putInt(NOTIFICATION_ID, 0)
                .putString(NotifyWork.NOTIFICATION_TITLE, notificationText)
                .build()
            val delay = customTime - currentTime
            Log.e("delay", "Delay: $delay")
            scheduleNotification(delay, data)

            val titleNotificationSchedule = "Scheduled expiry notification"

            viewModelScope.launch {
                addEditChannel.send(
                    AddEditInvLineItemEvent.ShowToastMessage(
                        titleNotificationSchedule
                    )
                )
            }

        } else {
            val errorNotificationSchedule = "Notification failed"
            viewModelScope.launch {
                addEditChannel.send(
                    AddEditInvLineItemEvent.ShowToastMessage(
                        errorNotificationSchedule
                    )
                )
            }
        }
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance()
        instanceWorkManager.beginWith(
            notificationWork
        ).enqueue()
    }

    fun getIngredientName(ingredientId: Int): LiveData<String> {
        return ingredientNamesAndIds.map { namesAndIds ->
            namesAndIds.find { it.first == ingredientId }?.second ?: ""
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


    sealed class AddEditInvLineItemEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditInvLineItemEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditInvLineItemEvent()

        data class ShowToastMessage(val msg: String) : AddEditInvLineItemEvent()

    }
}