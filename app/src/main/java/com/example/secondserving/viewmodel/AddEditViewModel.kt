package com.example.secondserving.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.secondserving.auth.AuthRepository
import com.example.secondserving.data.IngredientDAO
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val inventoryDAO: InventoryDAO,
    private val ingredientDAO: IngredientDAO
): ViewModel() {

    private suspend fun insertInventory(inventory: Inventory) {
        try {
            inventoryDAO.insertInventory(inventory)
            withContext(Dispatchers.Main) {
             //   clearFields()
            }
            Log.d("AddInventoryActivity", "Inventory added successfully: $inventory")
        } catch (e: Exception) {
            Log.e("AddInventoryActivity", "Error adding inventory", e)
            withContext(Dispatchers.Main) {
            }
        }
    }
}