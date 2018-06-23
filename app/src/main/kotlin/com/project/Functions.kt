package com.project

import com.project.data.valueobjects.LatLng
import com.project.data.valueobjects.Order
import com.project.data.valueobjects.OrderItem
import java.util.*

fun createFakeOrders(quantity: Int) = (1..quantity).map { createFakeOrder() }

fun createFakeOrder(): Order {
  val items = (1..3).map { fakeOrderLine() }
  val totalAmount = items.sumBy { it.price * it.quantity }
  return Order(
      id = UUID.randomUUID().toString(),
      consumerName = "Consumer #${randomNumber()}",
      items = items,
      totalAmount = totalAmount,
      change = totalAmount / 10,
      location = LatLng(
          lat = randomDouble(),
          lng = randomDouble()
      )
  )
}

fun fakeOrderLine(): OrderItem {
  return OrderItem(
      name = "Item #${randomNumber(100)}",
      price = randomNumber(10000),
      quantity = randomNumber(3)
  )
}

fun randomNumber(bound: Int = 1000) = Random().nextInt(bound)
fun randomDouble() = Random().nextDouble()
