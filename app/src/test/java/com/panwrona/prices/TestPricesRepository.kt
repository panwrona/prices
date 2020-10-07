package com.panwrona.prices

import com.panwrona.prices.entity.Direction
import com.panwrona.prices.entity.Price
import com.panwrona.prices.repository.PricesRepositoryImpl
import com.panwrona.prices.repository.RandomProvider
import io.reactivex.rxjava3.schedulers.TestScheduler
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.junit.Test
import java.util.concurrent.TimeUnit

class TestPricesRepository {

    @Test
    fun `should emit price with direction unknown as the first item`() {
        val testScheduler = TestScheduler()
        val repository = PricesRepositoryImpl(RandomProvider(), testScheduler)
        val testSubscriber = TestSubscriber<Price>()

        repository.prices("gold").subscribe(testSubscriber)
        repository.startEmission()

        testSubscriber.assertValue {
            it.direction == Direction.UNKNOWN
        }
    }

    @Test
    fun `should not emit prices when emission is stopped`() {
        val testScheduler = TestScheduler()
        val repository = PricesRepositoryImpl(RandomProvider(), testScheduler)
        val testSubscriber = TestSubscriber<Price>()

        repository.prices("gold").subscribe(testSubscriber)
        repository.startEmission()
        testSubscriber.assertValueCount(1)

        repository.stopEmission()
        testScheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS)

        repository.startEmission()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testSubscriber.assertValueCount(2)
    }

    @Test
    fun `should not emit prices downstream if values are the same`() {
        val testScheduler = TestScheduler()
        val repository = PricesRepositoryImpl(TestRandomProvider(), testScheduler)
        val testSubscriber = TestSubscriber<Price>()

        repository.prices("gold").subscribe(testSubscriber)
        repository.startEmission()
        testScheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS)
        testSubscriber.assertValueCount(1)
    }
}