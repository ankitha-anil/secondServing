package com.example.secondserving

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import com.example.secondserving.databinding.ActivityEditInventoryBinding
import com.example.secondserving.ui.SecondServingApplication
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class EditInventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditInventoryBinding
    private lateinit var inventoryDao: InventoryDAO
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var currentInventory: Inventory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            onBackPressed()
        }


      //  inventoryDao = (applicationContext as SecondServingApplication).data.inventoryDao()

        currentInventory = intent.getParcelableExtra("inventory") ?: return
        setupViews(currentInventory)

        binding.editProductBtn.setOnClickListener {
            val newName = binding.titleEt.text.toString().trim()
            if (newName.isNotEmpty()) {
                val updatedInventory = currentInventory.copy(name = newName)
                coroutineScope.launch {
                    updateInventory(updatedInventory)
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViews(inventory: Inventory) {
        binding.titleEt.setText(inventory.name)
    }

    private suspend fun updateInventory(inventory: Inventory) {
        try {
            inventoryDao.updateInventory(inventory)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EditInventoryActivity, "Inventory updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            Log.d("UpdateInventoryActivity", "Inventory updated successfully: $inventory")
        } catch (e: Exception) {
            Log.e("UpdateInventoryActivity", "Error updating inventory", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EditInventoryActivity, "Error updating inventory: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}