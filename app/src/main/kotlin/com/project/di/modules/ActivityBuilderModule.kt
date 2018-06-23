package com.project.di.modules

import com.project.di.scopes.ActivityScope
import com.project.feature.orderlist.OrderListActivity
import com.project.feature.orderlist.OrderListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = [ OrderListModule::class ])
  internal abstract fun orderListActivity(): OrderListActivity
}
