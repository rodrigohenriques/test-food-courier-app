package com.project.data.valueobjects

import android.os.Parcel
import android.os.Parcelable

data class Order(
    val id: String,
    val consumerName: String,
    val location: LatLng,
    val items: List<OrderItem>,
    val totalAmount: Int,
    val change: Int,
    val addressText: String
) : Parcelable {
  constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readString(),
      parcel.readParcelable(LatLng::class.java.classLoader),
      parcel.createTypedArrayList(OrderItem),
      parcel.readInt(),
      parcel.readInt(),
      parcel.readString()
  )

  companion object CREATOR : Parcelable.Creator<Order> {
    override fun createFromParcel(parcel: Parcel): Order {
      return Order(parcel)
    }

    override fun newArray(size: Int): Array<Order?> {
      return arrayOfNulls(size)
    }

    fun create(orderResponse: OrderResponse, addressText: String) = Order(
        id = orderResponse.id,
        consumerName = orderResponse.consumerName,
        location = orderResponse.location,
        items = orderResponse.items,
        totalAmount = orderResponse.totalAmount,
        change = orderResponse.change,
        addressText = addressText
    )
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(id)
    parcel.writeString(consumerName)
    parcel.writeParcelable(location, flags)
    parcel.writeTypedList(items)
    parcel.writeInt(totalAmount)
    parcel.writeInt(change)
    parcel.writeString(addressText)
  }

  override fun describeContents(): Int {
    return 0
  }
}

