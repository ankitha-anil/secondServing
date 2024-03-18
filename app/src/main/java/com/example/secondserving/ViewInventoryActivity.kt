package com.example.secondserving

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.secondserving.data.Inventory
import com.example.secondserving.data.InventoryDAO
import com.example.secondserving.databinding.ActivityEditInventoryBinding
import com.example.secondserving.databinding.BsInventoryDetailsBinding
import com.example.secondserving.ui.SecondServingApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewInventoryActivity : AppCompatActivity() {

    val editInventory = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            binding.titleTv.setText(result.data?.extras?.getString("newInventoryTitle"))
        }
    }

    private lateinit var binding: BsInventoryDetailsBinding
    private lateinit var inventoryDao: InventoryDAO
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var currentInventory: Inventory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BsInventoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton: ImageButton = findViewById(R.id.backBtn)
        backButton.setOnClickListener {
            onBackPressed()
        }


        inventoryDao = (applicationContext as SecondServingApplication).data.inventoryDao()

        currentInventory = intent.getParcelableExtra("inventory") ?: return
        setupViews(currentInventory)

        binding.editBtn.setOnClickListener {
//            val newName = binding.titleTv.text.toString().trim()
//            if (newName.isNotEmpty()) {
//                val updatedInventory = currentInventory.copy(name = newName)
//                coroutineScope.launch {
//                    updateInventory(updatedInventory)
//                }
//            } else {
//                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
//            }
            val intent = Intent(baseContext, EditInventoryActivity::class.java)
            intent.putExtra("inventory", currentInventory)
            editInventory.launch(intent)
        }
    }

    private fun setupViews(inventory: Inventory) {
        binding.titleTv.setText(inventory.name)
    }

    private suspend fun updateInventory(inventory: Inventory) {
        try {
            inventoryDao.updateInventory(inventory)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ViewInventoryActivity, "Inventory updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            Log.d("UpdateInventoryActivity", "Inventory updated successfully: $inventory")
        } catch (e: Exception) {
            Log.e("UpdateInventoryActivity", "Error updating inventory", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ViewInventoryActivity, "Error updating inventory: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}