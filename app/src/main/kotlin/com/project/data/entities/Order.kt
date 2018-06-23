package com.project.data.entities

data class Order(
    val id: String,
    val consumerName: String,
    val eta: String,
    val location: LatLng,
    val items: List<OrderItem>,
    val totalAmount: Int,
    val change: Int
)

