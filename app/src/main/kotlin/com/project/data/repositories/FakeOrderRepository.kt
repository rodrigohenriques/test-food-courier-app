package com.project.data.repositories

import com.project.data.valueobjects.Order
import com.project.createFakeOrderResponse
import com.project.data.valueobjects.OrderResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FakeOrderRepository @Inject constructor(
    private val googleRepository: GoogleRepository
) : OrderRepository {
  override fun delivered(order: Order): Completable = Completable.timer(3, TimeUnit.SECONDS)

  // This method has to be paginated to avoid a huge amount of requests to google apis
  override fun getOrders(): Single<List<Order>> {
    val orders = getFakeOrders().cache()
    val addresses = orders.flatMapSingle { googleRepository.reverseGeocode(it.location) }

    return orders
        .zipWith<String, Order>(addresses, BiFunction { orderResponse, address -> Order.create(orderResponse, address) })
        .toList()
  }

  private fun getFakeOrders(): Observable<OrderResponse> {
    return Single.timer(1, TimeUnit.SECONDS)
        .map { (1..10).map { createFakeOrderResponse() } }
        .flatMapObservable { Observable.fromIterable(it) }
  }
}

