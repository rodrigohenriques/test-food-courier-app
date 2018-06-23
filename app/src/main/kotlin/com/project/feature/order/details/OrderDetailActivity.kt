package com.project.feature.order.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.project.R
import com.project.data.valueobjects.Order
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_order_detail)

    val order = intent.getParcelableExtra<Order>(ORDER_EXTRA)

    order?.let { render(it) }
  }

  private fun render(order: Order) {
    textViewId.text = order.id
    textViewConsumerName.text = order.consumerName
    textViewAddress.text = order.addressText
    textViewItems.text = order.items.joinToString("\n")
  }

  companion object {
    const val ORDER_EXTRA = "order"
  }
}
