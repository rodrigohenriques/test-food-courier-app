package com.project.data.valueobjects

data class Order(
    val id: String,
    val consumerName: String,
    val location: LatLng,
    val addressText: String,
    val items: List<Any>,
    val totalAmount: Int,
    val change: Int
)

