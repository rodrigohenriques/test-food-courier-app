package com.project.feature.order.list

import com.project.data.valueobjects.Order
import io.reactivex.Observable

object OrderListContract {
  interface View {
    fun swipeRefreshes(): Observable<Unit>
    fun orderDeliveredClicks(): Observable<Order>
    fun orderClicks(): Observable<Order>
  }

  interface Presenter {
    fun onCreate()

    fun onDestroy()
  }
}
