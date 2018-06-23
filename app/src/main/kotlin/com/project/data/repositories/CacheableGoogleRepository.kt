package com.project.data.repositories

import com.project.BuildConfig
import com.project.apis.GoogleApi
import com.project.data.cache.LocationAddressCache
import com.project.data.valueobjects.LatLng
import io.reactivex.Single
import javax.inject.Inject

class CacheableGoogleRepository @Inject constructor(
    private val cache: LocationAddressCache,
    private val googleApi: GoogleApi
) : GoogleRepository {
  override fun reverseGeocode(latLng: LatLng): Single<String> {
    return cache.tryGet(latLng).switchIfEmpty(getAndCache(latLng))
  }

  private fun getAndCache(latLng: LatLng): Single<String?> {
    return googleApi.getReverseGeocode(BuildConfig.GOOGLE_MAPS_API_KEY, "${latLng.lat},${latLng.lng}")
        .map { it.results?.firstOrNull()?.formattedAddress }
        .flatMap { cache.store(latLng, it).andThen(Single.just(it)) }

  }
}