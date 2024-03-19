package com.example.secondserving.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.secondserving.data.InventoryLineItem
import com.example.secondserving.databinding.ItemInventoryListBinding

class InventoryLineItemAdapter(private val listener: OnItemClickListener) :
    ListAdapter<InventoryLineItem, InventoryLineItemAdapter.InventoryViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val binding =
            ItemInventoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class InventoryViewHolder(private val binding: ItemInventoryListBinding) : // without inner is like static, can access taskadapter from outside
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

        fun bind(inventoryLineItem: InventoryLineItem) {
            binding.apply {
                itemName.text = inventoryLineItem.inventoryID.toString()
                quantity.text = inventoryLineItem.quantity.toString()
                expiryDate.text = inventoryLineItem.expiryDate.toString()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(inventoryLineItem: InventoryLineItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<InventoryLineItem>() {
        override fun areItemsTheSame(oldItem: InventoryLineItem, newItem: InventoryLineItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: InventoryLineItem, newItem: InventoryLineItem) = oldItem == newItem

    }

}