package com.project.feature.order.list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.R
import com.project.data.valueobjects.Order
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_order_list.view.*

class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.Holder>() {

  private var data: List<Order> = emptyList()

  private val itemClicksSubject = PublishSubject.create<Order>()
  private val markAsDeliveredClicksSubject = PublishSubject.create<Order>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val itemView = layoutInflater.inflate(R.layout.item_order_list, parent, false)
    return Holder(itemView)
  }

  override fun onBindViewHolder(holder: Holder, position: Int) {
    val suggestion = data[position]
    holder.bind(suggestion)
  }

  override fun getItemCount(): Int = data.size

  fun changeData(newData: List<Order>) {
    val diffCallback = DiffCallback(data, newData)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    data = newData

    diffResult.dispatchUpdatesTo(this)
  }

  fun itemClicks(): Observable<Order> = itemClicksSubject

  fun orderDeliveredClicks(): Observable<Order> = markAsDeliveredClicksSubject

  inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(order: Order) {
      itemView.textViewConsumerName.text = order.consumerName
      itemView.textViewAddress.text = order.addressText
      itemView.buttonMarkAsDelivered.setOnClickListener { markAsDeliveredClicksSubject.onNext(order) }
      itemView.setOnClickListener { itemClicksSubject.onNext(order) }
    }
  }

  private inner class DiffCallback(
      val oldList: List<Order>,
      val newList: List<Order>
  ) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition] == newList[newItemPosition]
    }
  }
}