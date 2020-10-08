package com.panwrona.prices.repository

import com.panwrona.prices.TestRandomProvider
import com.panwrona.prices.entity.Price
import io.reactivex.rxjava3.schedulers.TestScheduler
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.junit.Test
import java.util.concurrent.TimeUnit

class PricesRepositoryTest {

   @Test
   fun `should not emit the price downstream if the amount is the same as previous`() {
       val testScheduler = TestScheduler()
       val testSubscriber = TestSubscriber<Price>()
       val repo: PricesRepository = PricesRepositoryImpl(TestRandomProvider(), testScheduler)

       repo.prices("test").subscribe(testSubscriber)
       testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
       testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
       testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

       testSubscriber.assertValueCount(1)
   }
}