package com.project.feature.orderlist

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.project.Error
import com.project.StateManager
import com.project.Success
import com.project.createFakeOrders
import com.project.data.repositories.OrderRepository
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
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
  private var stateManager = StateManager(OrderListState())

  @Mock
  private lateinit var view: OrderListContract.View

  @InjectMocks
  private lateinit var orderListPresenter: OrderListPresenter

  private val testScheduler: TestScheduler = TestScheduler()

  @Before
  fun setup() {
    stateManager.setState(OrderListState())
    whenever(schedulerProvider.get()).thenReturn(testScheduler)
  }

  @Test
  fun getOrders_withSuccessfulRequest_ShouldUpdateStateWithSuccessAndData() {
    val orders = createFakeOrders(3)

    whenever(orderRepository.getOrders()).thenReturn(Single.just(orders))

    val testObserver = orderListPresenter.getOrders().test()

    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

    testObserver.assertComplete()

    verify(orderRepository).getOrders()

    assertThat(stateManager.state().orders, `is`(orders))
    assert(stateManager.state().type === Success)
  }

  @Test
  fun getOrders_withFailedRequest_ShouldUpdateStateWithErrorAndNoData() {
    whenever(orderRepository.getOrders()).thenReturn(Single.error(Exception()))

    val testObserver = orderListPresenter.getOrders().test()

    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

    testObserver.assertComplete()

    verify(orderRepository).getOrders()

    assertThat(stateManager.state().orders.size, `is`(0))
    assertThat(stateManager.state().type, instanceOf(Error::class.java))
  }

  @Test
  fun orderDelivered_successfulRequest_ShouldRemoveFromOrderList() {
    val orders = createFakeOrders(3)

    stateManager.setState { it.copy(type = Success, orders = orders) }

    val target = orders.first()

    whenever(orderRepository.delivered(target)).thenReturn(Completable.complete())

    val testObserver = orderListPresenter.orderDelivered(target).test()

    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

    testObserver.assertComplete()

    verify(orderRepository).delivered(target)

    assertThat(stateManager.state().orders.size, `is`(2))
    assertThat(stateManager.state().orders.contains(target), `is`(false))
    assert(stateManager.state().type === Success)
  }

  @Test
  fun orderDelivered_failedRequest_ShouldRemoveFromOrderList() {
    val orders = createFakeOrders(3)

    stateManager.setState { it.copy(type = Success, orders = orders) }

    val target = orders.first()

    whenever(orderRepository.delivered(target)).thenReturn(Completable.error(Exception()))

    val testObserver = orderListPresenter.orderDelivered(target).test()

    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

    testObserver.assertComplete()

    verify(orderRepository).delivered(target)

    assertThat(stateManager.state().orders.size, `is`(3))
    assertThat(stateManager.state().orders.contains(target), `is`(true))
    assertThat(stateManager.state().type, instanceOf(Error::class.java))
  }
}