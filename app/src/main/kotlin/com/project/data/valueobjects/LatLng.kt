package com.project.data.valueobjects

data class LatLng(
    val lat: Double,
    val lng: Double
) {
  override fun toString(): String {
    return "$lat,$lng"
  }
}

