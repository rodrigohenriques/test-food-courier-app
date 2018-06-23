package com.project.feature.orderlist

import android.os.Bundle
import com.project.R
import dagger.android.support.DaggerAppCompatActivity

class OrderListActivity : DaggerAppCompatActivity(), OrderListContract.View {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
