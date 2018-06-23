package com.project.di.modules

import android.app.Application
import android.content.Context
import com.project.BuildConfig
import com.project.apis.GoogleMapsApi
import com.project.data.cache.LocationAddressCache
import com.project.data.cache.LocationAddressDummyCache
import com.project.data.repositories.CacheableGoogleRepository
import com.project.data.repositories.FakeOrderRepository
import com.project.data.repositories.GoogleRepository
import com.project.data.repositories.OrderRepository
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule {
  @Provides
  fun provideApplicationContext(application: Application): Context = application

  @Provides
  fun provideOrderRepository(googleRepository: GoogleRepository): OrderRepository {
    return FakeOrderRepository(googleRepository)
  }

  @Provides
  fun provideGoogleRepository(cacheableGoogleRepository: CacheableGoogleRepository): GoogleRepository {
    return cacheableGoogleRepository
  }

  @Provides
  @Singleton
  fun provideLocationAddressCache(locationAddressDummyCache: LocationAddressDummyCache): LocationAddressCache {
    return locationAddressDummyCache
  }

  @Provides
  fun provideBackgroundScheduler(): Scheduler = Schedulers.io()

  @Provides
  fun provideOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()

    httpLoggingInterceptor.level = when {
      BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
      else -> HttpLoggingInterceptor.Level.NONE
    }

    return OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()
  }

  @Provides
  fun providesGoogleApi(client: OkHttpClient) : GoogleMapsApi {
    return Retrofit.Builder()
        .baseUrl(GoogleMapsApi.URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(GoogleMapsApi::class.java)
  }
}