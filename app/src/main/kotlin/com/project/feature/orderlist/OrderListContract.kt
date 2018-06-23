package com.project.feature.orderlist

object OrderListContract {
  interface View {

  }

  interface Presenter {
    fun attachView(view: View)

    fun detachView()
  }
}
