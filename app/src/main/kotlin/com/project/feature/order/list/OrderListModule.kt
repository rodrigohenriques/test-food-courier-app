package com.project.feature.order.list

import com.project.StateManager
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
  fun provideStore(): StateManager<OrderListState> = StateManager(OrderListState())

  @Provides
  fun provideStateChanges(stateManager: StateManager<OrderListState>): Observable<OrderListState> = stateManager.updates()
}