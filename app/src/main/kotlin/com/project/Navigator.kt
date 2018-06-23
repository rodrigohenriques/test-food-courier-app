package com.project

import android.app.Activity
import android.content.Intent
import com.project.data.valueobjects.Order
import com.project.di.scopes.ActivityScope
import com.project.feature.order.details.OrderDetailActivity
import javax.inject.Inject

@ActivityScope
class Navigator @Inject constructor(private val activity: Activity) {
  fun navigateToOrderDetail(order: Order) {
    val intent = Intent(activity, OrderDetailActivity::class.java)
    intent.putExtra(OrderDetailActivity.ORDER_EXTRA, order)
    activity.startActivity(intent)
  }
}
