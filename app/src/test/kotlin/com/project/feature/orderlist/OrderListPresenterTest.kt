package com.project.feature.orderlist

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.project.Error
import com.project.StateManager
import com.project.Success
import com.project.createFakeOrders
import com.project.data.repositories.OrderRepository
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit
import javax.inject.Provider

@RunWith(MockitoJUnitRunner::class)
class OrderListPresenterTest {

  @Mock
  private lateinit var orderRepository: OrderRepository

  @Mock
  private lateinit var schedulerProvider: Provider<Scheduler>

  @Spy
  private var store = StateManager(OrderListState())

  @InjectMocks
  private lateinit var orderListPresenter: OrderListPresenter

  private val testScheduler: TestScheduler = TestScheduler()

  @Test
  fun getOrders_withSuccessfulRequest_ShouldUpdateStateWithSuccessAndData() {
    store.setState(OrderListState())

    whenever(schedulerProvider.get()).thenReturn(testScheduler)

    val fakeOrders = createFakeOrders(3)

    whenever(orderRepository.getOrders()).thenReturn(Single.just(fakeOrders))

    val testObserver = orderListPresenter.getOrders().test()

    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

    testObserver.assertComplete()

    verify(orderRepository).getOrders()

    val state = store.state()

    assertThat(state.orders.size, `is`(3))
    assert(state.type === Success)
  }

  @Test
  fun getOrders_withFailedRequest_ShouldUpdateStateWithErrorAndNoData() {
    store.setState(OrderListState())

    whenever(schedulerProvider.get()).thenReturn(testScheduler)

    whenever(orderRepository.getOrders()).thenReturn(Single.error(Exception()))

    val testObserver = orderListPresenter.getOrders().test()

    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

    testObserver.assertComplete()

    verify(orderRepository).getOrders()

    val state = store.state()

    assertThat(state.orders.size, `is`(0))
    assertThat(state.type, instanceOf(Error::class.java))
  }
}