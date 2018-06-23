package com.project.data.repositories

import com.project.data.entities.Order
import io.reactivex.Single

interface OrderRepository {
  fun getOrders() : Single<List<Order>>
}
