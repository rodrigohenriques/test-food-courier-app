package com.project.data.valueobjects

import android.os.Parcel
import android.os.Parcelable

data class LatLng(
    val lat: Double,
    val lng: Double
) : Parcelable {
  constructor(parcel: Parcel) : this(
      parcel.readDouble(),
      parcel.readDouble())

  override fun toString(): String {
    return "$lat,$lng"
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeDouble(lat)
    parcel.writeDouble(lng)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<LatLng> {
    override fun createFromParcel(parcel: Parcel): LatLng {
      return LatLng(parcel)
    }

    override fun newArray(size: Int): Array<LatLng?> {
      return arrayOfNulls(size)
    }
  }
}

