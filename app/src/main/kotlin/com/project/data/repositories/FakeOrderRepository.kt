package com.project.data.repositories

import com.project.data.valueobjects.Order
import com.project.createFakeOrder
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class FakeOrderRepository : OrderRepository {
  override fun delivered(order: Order): Completable = Completable.timer(3, TimeUnit.SECONDS)

  override fun getOrders(): Single<List<Order>> =
      Single.timer(5, TimeUnit.SECONDS)
          .map { (1..10).map { createFakeOrder() } }
}

