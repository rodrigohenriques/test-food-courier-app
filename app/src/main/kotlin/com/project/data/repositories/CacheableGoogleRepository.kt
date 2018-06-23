package com.project.data.repositories

import com.project.BuildConfig
import com.project.apis.GoogleMapsApi
import com.project.data.cache.LocationAddressCache
import com.project.data.valueobjects.LatLng
import io.reactivex.Single
import javax.inject.Inject

class CacheableGoogleRepository @Inject constructor(
    private val cache: LocationAddressCache,
    private val googleMapsApi: GoogleMapsApi
) : GoogleRepository {
  override fun reverseGeocode(latLng: LatLng): Single<String> {
    return cache.tryGet(latLng).switchIfEmpty(getAndCache(latLng))
  }

  private fun getAndCache(latLng: LatLng): Single<String?> {
    return googleMapsApi.getReverseGeocode(BuildConfig.GOOGLE_MAPS_API_KEY, latLng.toString())
        .map { it.results?.firstOrNull()?.formattedAddress }
        .flatMap { cache.store(latLng, it).andThen(Single.just(it)) }

  }
}