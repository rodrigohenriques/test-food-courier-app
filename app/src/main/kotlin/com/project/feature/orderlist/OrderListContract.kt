package com.project.feature.orderlist

import io.reactivex.Observable

object OrderListContract {
  interface View {
    fun pullToRefreshAction(): Observable<Unit>
  }

  interface Presenter {
    fun onCreate()

    fun onDestroy()
  }
}
