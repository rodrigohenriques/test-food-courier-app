package com.project.data.repositories

import com.project.data.valueobjects.Order
import io.reactivex.Completable
import io.reactivex.Single

interface OrderRepository {
  fun getOrders(): Single<List<Order>>
  fun delivered(order: Order): Completable
}
