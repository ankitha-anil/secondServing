package com.example.secondserving.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.secondserving.data.Inventory
import com.example.secondserving.databinding.ItemInventoryBinding

class InventoryAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Inventory, InventoryAdapter.InventoryViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val binding =
            ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class InventoryViewHolder(private val binding: ItemInventoryBinding) : // without inner is like static, can access taskadapter from outside
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

        fun bind(inventory: Inventory) {
            binding.apply {
                inventoryName.text = inventory.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(inventory: Inventory)
    }

    class DiffCallback : DiffUtil.ItemCallback<Inventory>() {
        override fun areItemsTheSame(oldItem: Inventory, newItem: Inventory) =
            oldItem.inventoryId == newItem.inventoryId

        override fun areContentsTheSame(oldItem: Inventory, newItem: Inventory) = oldItem == newItem

    }

}