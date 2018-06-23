package com.project.feature.orderlist

import com.project.Idle
import com.project.StateType
import com.project.data.valueobjects.Order

data class OrderListState(
    val type: StateType = Idle,
    val orders: List<Order> = emptyList()
)
