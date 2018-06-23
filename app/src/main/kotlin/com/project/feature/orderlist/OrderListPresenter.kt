package com.project.feature.orderlist

import com.project.Error
import com.project.Loading
import com.project.Store
import com.project.Success
import com.project.data.repositories.OrderRepository
import com.project.data.valueobjects.Order
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Provider

class OrderListPresenter @Inject constructor(
    private val orderRepository: OrderRepository,
    private val backgroundScheduler: Provider<Scheduler>,
    private val store: Store<OrderListState>
) : OrderListContract.Presenter {
  private val disposables = CompositeDisposable()

  override fun attachView(view: OrderListContract.View) {
    getOrders().subscribe().addTo(disposables)
  }

  override fun detachView() {
    disposables.clear()
  }

  fun getOrders(): Completable {
    return orderRepository.getOrders()
        .doOnSubscribe { store.update { copy(type = Loading) } }
        .subscribeOn(backgroundScheduler.get())
        .retry(3)
        .doOnError { store.update { copy(type = Error("Can't load orders"), orders = emptyList()) } }
        .doOnSuccess { orders -> store.update { copy(type = Success, orders = orders) } }
        .toCompletable()
        .onErrorComplete()
  }
}