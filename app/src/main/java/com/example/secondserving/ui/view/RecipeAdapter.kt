package com.example.secondserving.ui.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.secondserving.data.Recipe
import com.example.secondserving.databinding.ItemRecipeBinding

class RecipeAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) : // without inner is like static, can access taskadapter from outside
        RecyclerView.ViewHolder(binding.root) {

        init { //can avoid calling the onclicklistener multiple times
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) { //-1 position is no position
                        val inventory = getItem(position)
                        listener.onItemClick(inventory)
                    }
                }
            }
        }

        fun bind(recipe: Recipe) {
            binding.apply {
                textViewRecipeName.text = recipe.recipeName
                textViewRecipeDescription.text = recipe.recipeDescription
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(inventory: Recipe)
    }

    class DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem

    }

}