package com.example.secondserving.ui.view

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.secondserving.data.InvLineItemDisplay
import com.example.secondserving.data.InventoryLineItem
import com.example.secondserving.databinding.ItemInventoryListBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class InventoryLineItemAdapter(private val listener: OnItemClickListener) :
    ListAdapter<InvLineItemDisplay, InventoryLineItemAdapter.InventoryViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val binding =
            ItemInventoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(invLineItemDisplay: InvLineItemDisplay) {
            binding.apply {
                val instant = Instant.ofEpochMilli(invLineItemDisplay.expiryDate)
                val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                val formattedDateTime = dateTime.format(formatter)

                itemName.text = invLineItemDisplay.name.toString()
                quantity.text = "Quantity: ${invLineItemDisplay.quantity}"
                expiryDate.text = "Expiry Date: $formattedDateTime"
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(inventoryLineItem: InvLineItemDisplay)
    }

    class DiffCallback : DiffUtil.ItemCallback<InvLineItemDisplay>() {
        override fun areItemsTheSame(oldItem: InvLineItemDisplay, newItem: InvLineItemDisplay) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: InvLineItemDisplay, newItem: InvLineItemDisplay) = oldItem == newItem

    }

}