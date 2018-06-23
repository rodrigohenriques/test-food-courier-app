package com.project.di.modules

import android.app.Application
import android.content.Context
import com.project.data.repositories.FakeOrderRepository
import com.project.data.repositories.OrderRepository
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Module
class ApplicationModule {
  @Provides
  fun provideApplicationContext(application: Application): Context = application

  @Provides
  fun provideOrderRepository(): OrderRepository {
    return FakeOrderRepository()
  }

  @Provides
  fun provideBackgroundScheduler(): Scheduler = Schedulers.io()
}