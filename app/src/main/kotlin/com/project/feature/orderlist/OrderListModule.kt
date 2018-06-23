package com.project.feature.orderlist

import android.app.Activity
import com.project.Store
import com.project.di.scopes.ActivityScope
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class OrderListModule {

  @Provides
  @ActivityScope
  fun provideView(activity: OrderListActivity): OrderListContract.View = activity

  @Provides
  @ActivityScope
  fun providePresenter(orderListPresenter: OrderListPresenter): OrderListContract.Presenter {
    return orderListPresenter
  }

  @Provides
  @ActivityScope
  fun provideStore(): Store<OrderListState> = Store(OrderListState())

  @Provides
  fun provideStateChanges(store: Store<OrderListState>): Observable<OrderListState> = store.stateChanges()
}