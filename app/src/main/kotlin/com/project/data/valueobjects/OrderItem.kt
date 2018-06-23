package com.project.data.valueobjects

import android.os.Parcel
import android.os.Parcelable

data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Int
) : Parcelable {
  constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readInt(),
      parcel.readInt())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(name)
    parcel.writeInt(quantity)
    parcel.writeInt(price)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<OrderItem> {
    override fun createFromParcel(parcel: Parcel): OrderItem {
      return OrderItem(parcel)
    }

    override fun newArray(size: Int): Array<OrderItem?> {
      return arrayOfNulls(size)
    }
  }
}