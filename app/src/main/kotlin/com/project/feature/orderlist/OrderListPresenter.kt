package com.project.feature.orderlist

import com.project.Error
import com.project.Loading
import com.project.Store
import com.project.Success
import com.project.data.repositories.OrderRepository
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import javax.inject.Provider

class OrderListPresenter @Inject constructor(
    private val orderRepository: OrderRepository,
    private val backgroundScheduler: Provider<Scheduler>,
    private val store: Store<OrderListState>,
    private val view: OrderListContract.View
) : OrderListContract.Presenter {
  private val disposables = CompositeDisposable()

  override fun onCreate() {
    getOrders().subscribe().addTo(disposables)

    view.pullToRefreshAction()
        .flatMapCompletable { getOrders() }
        .subscribe()
        .addTo(disposables)
  }

  override fun onDestroy() {
    disposables.clear()
  }

  fun getOrders(): Completable {
    return orderRepository.getOrders()
        .subscribeOn(backgroundScheduler.get())
        .doOnSubscribe { store.update { copy(type = Loading) } }
        .retry(3)
        .doOnError { store.update { copy(type = Error("Can't load orders"), orders = emptyList()) } }
        .doOnSuccess { orders -> store.update { copy(type = Success, orders = orders) } }
        .toCompletable()
        .onErrorComplete()
  }
}