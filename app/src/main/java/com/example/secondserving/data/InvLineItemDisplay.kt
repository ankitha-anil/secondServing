package com.example.secondserving.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class InvLineItemDisplay(
    val name: String,
    val expiryDate: Long,
    val quantity: Int,
) : Parcelable {
}