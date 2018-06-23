package com.project.data.valueobjects

data class OrderResponse(
    val id: String,
    val consumerName: String,
    val location: LatLng,
    val items: List<OrderItem>,
    val totalAmount: Int,
    val change: Int
)
