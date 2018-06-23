package com.project.feature.orderlist

import android.os.Bundle
import com.project.Loading
import com.project.R
import com.project.Success
import com.project.data.valueobjects.Order
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class OrderListActivity : DaggerAppCompatActivity(), OrderListContract.View {

  @Inject lateinit var stateChanges: Observable<OrderListState>
  @Inject lateinit var presenter: OrderListContract.Presenter

  private val disposables = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    presenter.attachView(this)

    stateChanges
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { render(it) }
        .subscribe()
        .addTo(disposables)
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.detachView()
    disposables.clear()
  }

  private fun render(state: OrderListState) {
    when (state.type) {
      is Loading -> showLoading()
      is Success -> {
        hideLoading()
        showOrders(state.orders)
      }
      is Error -> {
        hideLoading()
        showError(state.type.message)
      }
    }
  }

  private fun showError(message: String?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun showOrders(orders: List<Order>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun hideLoading() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun showLoading() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
