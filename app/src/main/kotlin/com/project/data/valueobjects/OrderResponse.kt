package com.project.data.valueobjects

data class OrderResponse(
    val id: String,
    val consumerName: String,
    val location: LatLng,
    val items: List<Any>,
    val totalAmount: Int,
    val change: Int
)
