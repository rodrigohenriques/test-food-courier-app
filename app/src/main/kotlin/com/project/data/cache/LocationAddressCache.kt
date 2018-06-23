package com.project.data.cache

import com.project.data.valueobjects.LatLng
import io.reactivex.Completable
import io.reactivex.Maybe
import javax.inject.Inject

interface LocationAddressCache {
  fun tryGet(latLng: LatLng): Maybe<String>

  fun store(latLng: LatLng, address: String): Completable
}

// I'm aware that this cache method isn't good at all
// But I just want to avoid to reload all addresses every time
// I swipe to refresh my order list
class LocationAddressDummyCache @Inject constructor() : LocationAddressCache {
  override fun store(latLng: LatLng, address: String): Completable {
    return Completable.fromAction {
      CACHE[latLng] = address
    }
  }

  override fun tryGet(latLng: LatLng): Maybe<String> {
    return Maybe.create {emitter ->
      if (CACHE.containsKey(latLng) && CACHE[latLng] != null)
        emitter.onSuccess(CACHE[latLng]!!)
      else
        emitter.onComplete()
    }
  }

  companion object {
    private val CACHE: MutableMap<LatLng, String> = HashMap()
  }
}