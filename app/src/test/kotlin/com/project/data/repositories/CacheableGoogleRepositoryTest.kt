package com.project.data.repositories

import com.nhaarman.mockito_kotlin.*
import com.project.BuildConfig
import com.project.apis.GoogleMapsApi
import com.project.data.cache.LocationAddressCache
import com.project.data.valueobjects.GeocodeResponse
import com.project.data.valueobjects.GeocodeResult
import com.project.data.valueobjects.LatLng
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CacheableGoogleRepositoryTest {

  @Mock
  lateinit var locationAddressCache: LocationAddressCache

  @Mock
  lateinit var googleMapsApi: GoogleMapsApi

  @InjectMocks
  lateinit var cacheableGoogleRepository: CacheableGoogleRepository

  private val testScheduler: TestScheduler = TestScheduler()

  @Test
  fun reverseGeocode_withoutCache_shouldCallApiAndCacheResult() {
    val latLng = LatLng(lat = 12.023, lng = 10.123)

    whenever(locationAddressCache.tryGet(latLng)).thenReturn(Maybe.empty())

    val formattedAddress = "formatted address"

    whenever(locationAddressCache.store(latLng, formattedAddress)).thenReturn(Completable.complete())

    val response = createFakeGeocodeResponse(formattedAddress)

    whenever(googleMapsApi.getReverseGeocode(eq(BuildConfig.GOOGLE_MAPS_API_KEY), eq(latLng.toString())))
        .thenReturn(Single.just(response))

    val testObserver = cacheableGoogleRepository.reverseGeocode(latLng).test()

    verify(googleMapsApi).getReverseGeocode(eq(BuildConfig.GOOGLE_MAPS_API_KEY), eq(latLng.toString()))
    verify(locationAddressCache).tryGet(latLng)
    verify(locationAddressCache).store(latLng, formattedAddress)

    testObserver.assertValue(formattedAddress)
    testObserver.assertComplete()
  }

  @Test
  fun reverseGeocode_withCache_shouldCallApiAndCacheResult() {
    val latLng = LatLng(lat = 12.023, lng = 10.123)
    val formattedAddress = "formatted address"

    whenever(locationAddressCache.tryGet(latLng)).thenReturn(Maybe.just(formattedAddress))
    whenever(googleMapsApi.getReverseGeocode(any(), any())).thenReturn(Single.error(Exception()))

    val testObserver = cacheableGoogleRepository.reverseGeocode(latLng).test()

    verify(locationAddressCache).tryGet(latLng)
    verify(locationAddressCache, never()).store(latLng, formattedAddress)

    testObserver.assertValue(formattedAddress)
    testObserver.assertComplete()
  }

  private fun createFakeGeocodeResponse(formattedAddress: String) : GeocodeResponse {
    val results = listOf(
        GeocodeResult(
            emptyList(),
            formattedAddress = formattedAddress,
            geometry = null,
            partialMatch = false,
            placeId = UUID.randomUUID().toString(),
            types = emptyList()
        )
    )

    return GeocodeResponse(GoogleMapsApi.OK, results)
  }

}