package com.project.apis

import com.project.data.valueobjects.GeocodeResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {
  @GET("maps/api/geocode/json")
  fun getReverseGeocode(
      @Query("key") key: String,
      @Query("latlng") latLng: String
  ): Single<GeocodeResponse>

  companion object {
    const val URL = "https://maps.googleapis.com"

    const val OK = "OK"
    const val ZERO_RESULTS = "ZERO_RESULTS"
    const val OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT"
    const val REQUEST_DENIED = "REQUEST_DENIED"
    const val INVALID_REQUEST = "INVALID_REQUEST"
    const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
  }
}