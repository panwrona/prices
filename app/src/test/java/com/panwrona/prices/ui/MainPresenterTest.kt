package com.panwrona.prices.ui

import com.nhaarman.mockitokotlin2.*
import com.panwrona.prices.TestRandomProvider
import com.panwrona.prices.TestSchedulersFacade
import com.panwrona.prices.entity.Direction
import com.panwrona.prices.entity.Price
import com.panwrona.prices.repository.PricesRepository
import com.panwrona.prices.repository.PricesRepositoryImpl
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit

class MainPresenterTest {

    @Test
    fun `should emit first value as unknown`() {
        val testScheduler = TestScheduler()
        val repo = createRepository(testScheduler)
        val presenter = MainPresenter(repo, TestSchedulersFacade(testScheduler))
        val view = mock<MainView>()
        presenter.attachView(view)
        presenter.startObserving()

        presenter.changeMarket("PLN")
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        verify(view).updatePrice(Price("PLN", 1, Direction.UNKNOWN))
    }

    @Test
    fun `should emit last emitted value when subscribing again`() {
        val testScheduler = TestScheduler()
        val repo = createRepository(testScheduler)
        val presenter = MainPresenter(repo, TestSchedulersFacade(testScheduler))
        val view = mock<MainView>()

        presenter.attachView(view)
        presenter.startObserving()
        presenter.changeMarket("PLN")
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        presenter.stopObserving()
        presenter.startObserving()
        presenter.stopObserving()
        presenter.startObserving()

        verify(view, times(3)).updatePrice(Price("PLN", 1, Direction.UNKNOWN))
    }

    @Test
    fun `should change emitted prices when market is changed`() {
        val testScheduler = TestScheduler()
        val repo = createRepository(testScheduler)
        val presenter = MainPresenter(repo, TestSchedulersFacade(testScheduler))
        val view = mock<MainView>()

        presenter.attachView(view)
        presenter.startObserving()
        presenter.changeMarket("PLN")
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        verify(view).updatePrice(Price("PLN", 1, Direction.UNKNOWN))

        presenter.changeMarket("GOLD")
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        verify(view).updatePrice(Price("GOLD", 1, Direction.UNKNOWN))
    }

    @Test
    fun `should change the direction of emitted values`() {
        val market = "PLN"
        val dummyPrices = Flowable.just(
            Price(market, 1, Direction.UNKNOWN),
            Price(market, 5, Direction.UNKNOWN),
            Price(market, 4, Direction.UNKNOWN)
        )
        val repo = mock<PricesRepository>()
        val presenter = MainPresenter(repo, TestSchedulersFacade(TestScheduler()))
        val view = mock<MainView>()
        whenever(repo.prices(market)).thenReturn(dummyPrices)

        presenter.attachView(view)
        presenter.startObserving()
        presenter.changeMarket(market)

        inOrder(view) {
            view.updatePrice(Price(market, 1, Direction.UNKNOWN))
            view.updatePrice(Price(market, 5, Direction.UP))
            view.updatePrice(Price(market, 4, Direction.DOWN))
        }
    }

    private fun createRepository(scheduler: TestScheduler): PricesRepository {
        return PricesRepositoryImpl(TestRandomProvider(), scheduler)
    }
}