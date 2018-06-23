package com.project.feature.order.list

import com.project.*
import com.project.data.repositories.OrderRepository
import com.project.data.valueobjects.Order
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import javax.inject.Provider

class OrderListPresenter @Inject constructor(
    private val orderRepository: OrderRepository,
    private val backgroundScheduler: Provider<Scheduler>,
    private val stateManager: StateManager<OrderListState>,
    private val view: OrderListContract.View,
    private val navigator: Navigator
) : OrderListContract.Presenter {
  private val disposables = CompositeDisposable()

  override fun onCreate() {
    getOrders().subscribe().addTo(disposables)

    view.swipeRefreshes()
        .flatMapCompletable { getOrders() }
        .subscribe()
        .addTo(disposables)

    view.orderDeliveredClicks()
        .flatMapCompletable { orderDelivered(it) }
        .subscribe()
        .addTo(disposables)

    view.orderClicks()
        .flatMapCompletable { openDetail(it) }
        .subscribe().addTo(disposables)
  }

  override fun onDestroy() {
    disposables.clear()
  }

  fun orderDelivered(order: Order): Completable {
    return orderRepository.delivered(order)
        .subscribeOn(backgroundScheduler.get())
        .doOnSubscribe { stateManager.setState { it.copy(type = Loading) } }
        .doOnError { stateManager.setState { it.copy(type = Error("Can't mark this order as delivered")) } }
        .doOnComplete {
          stateManager.setState { currentState ->
            currentState.copy(
                type = Success,
                orders = currentState.orders.filter { o -> o.id != order.id }
            )
          }
        }
        .onErrorComplete()
  }

  fun getOrders(): Completable {
    return orderRepository.getOrders()
        .subscribeOn(backgroundScheduler.get())
        .doOnSubscribe { stateManager.setState { it.copy(type = Loading) } }
        .retry(3)
        .doOnError { stateManager.setState { it.copy(type = Error("Can't load orders"), orders = emptyList()) } }
        .doOnSuccess { orders -> stateManager.setState { it.copy(type = Success, orders = orders) } }
        .toCompletable()
        .onErrorComplete()
  }

  fun openDetail(order: Order) : Completable {
    return Completable.fromAction { navigator.navigateToOrderDetail(order) }
        .subscribeOn(AndroidSchedulers.mainThread())
        .onErrorComplete()
  }
}