package com.project.data.valueobjects

data class Order(
    val id: String,
    val consumerName: String,
    val location: LatLng,
    val items: List<Any>,
    val totalAmount: Int,
    val change: Int,
    val addressText: String
) {
  companion object {
    fun create(orderResponse: OrderResponse, addressText: String) = Order(
        id = orderResponse.id,
        consumerName = orderResponse.consumerName,
        location = orderResponse.location,
        items = orderResponse.items,
        totalAmount = orderResponse.totalAmount,
        change = orderResponse.change,
        addressText = addressText
    )
  }
}

