package com.project.feature.order.list

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.project.Error
import com.project.Loading
import com.project.R
import com.project.Success
import com.project.data.valueobjects.Order
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class OrderListActivity : DaggerAppCompatActivity(), OrderListContract.View {

  @Inject
  lateinit var stateChanges: Observable<OrderListState>
  @Inject
  lateinit var presenter: OrderListContract.Presenter

  private val adapter = OrderListAdapter()
  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    presenter.onCreate()

    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter

    stateChanges
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { render(it) }
        .subscribe()
        .addTo(disposables)
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onDestroy()
    disposables.clear()
  }

  override fun swipeRefreshes(): Observable<Unit> =
      RxSwipeRefreshLayout.refreshes(swipeRefreshLayout)
          .map { Unit }

  override fun orderDeliveredClicks(): Observable<Order> = adapter.orderDeliveredClicks()

  override fun orderClicks(): Observable<Order> = adapter.itemClicks()

  private fun render(state: OrderListState) {
    when (state.type) {
      is Loading -> showLoading()
      is Success -> {
        hideLoading()
        showOrders(state.orders)
      }
      is Error -> {
        hideLoading()
        showMessage(state.type.message)
      }
    }
  }

  private fun showMessage(message: String) {
    Snackbar.make(swipeRefreshLayout, message, Snackbar.LENGTH_SHORT).show()
  }

  private fun showOrders(orders: List<Order>) {
    adapter.changeData(orders)
  }

  private fun hideLoading() {
    swipeRefreshLayout.isRefreshing = false
  }

  private fun showLoading() {
    swipeRefreshLayout.isRefreshing = true
  }
}
