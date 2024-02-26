package com.example.secondserving

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.secondserving.databinding.ActivityAddInventoryBinding

class AddInventoryActivity : AppCompatActivity() {

    // Using view binding to interact with the layout views
    private lateinit var binding: ActivityAddInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityAddInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root) // Corrected line

        // Set up the action bar if you have one
        supportActionBar?.title = "Add Inventory Item"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up a click listener on the 'Add Product' button
        binding.addProductBtn.setOnClickListener {
            // Get the text from input fields
            val title = binding.titleEt.text.toString().trim()
            val description = binding.descriptionEt.text.toString().trim()

            // Perform validation and then save the data or show a toast for demonstration
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Product added: $title", Toast.LENGTH_SHORT).show()

                // Optionally, clear the fields or close the activity
                binding.titleEt.text.clear()
                binding.descriptionEt.text.clear()

                // Close the activity if done
                finish()
            }
        }
    }

    // If you want to handle the 'up' button in the action bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
