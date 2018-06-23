package com.project.data.repositories

import com.project.data.valueobjects.LatLng
import io.reactivex.Single

interface GoogleRepository {
  fun reverseGeocode(latLng: LatLng): Single<String>
}