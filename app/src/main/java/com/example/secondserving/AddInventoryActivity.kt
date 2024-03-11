package com.example.secondserving

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import com.example.secondserving.databinding.ActivityAddInventoryBinding
import com.example.secondserving.ui.SecondServingApplication
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddInventoryActivity : AppCompatActivity() {

    // Using view binding to interact with the layout views
    private lateinit var binding: ActivityAddInventoryBinding
    private lateinit var inventoryDao: InventoryDAO // Assuming you have a DAO interface
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityAddInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Set up the action bar if you have one
        supportActionBar?.title = "Add Inventory Item"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize your Room database and obtain the InventoryDao instance
        inventoryDao = (applicationContext as SecondServingApplication).data.inventoryDao()

        // Set up a click listener on the 'Add Product' button
        binding.addProductBtn.setOnClickListener {
            val name = binding.titleEt.text.toString().trim()
            val currentUser=auth.currentUser
            val userId=currentUser?.uid ?: ""
            if (name.isNotEmpty()) {
                val inventory = Inventory(name = name, userID = userId)
                coroutineScope.launch {
                    insertInventory(inventory)
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun insertInventory(inventory: Inventory) {
        try {
            inventoryDao.insertInventory(inventory)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddInventoryActivity, "Inventory added successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            Log.d("AddInventoryActivity", "Inventory added successfully: $inventory")
        } catch (e: Exception) {
            Log.e("AddInventoryActivity", "Error adding inventory", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddInventoryActivity, "Error adding inventory: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        binding.titleEt.text.clear()
    }

    // If you want to handle the 'up' button in the action bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
