package com.panwrona.prices.repository

import com.panwrona.prices.entity.Direction
import com.panwrona.prices.entity.Price
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class PricesRepositoryImpl(
    private val randomProvider: RandomProvider = RandomProvider(),
    private val scheduler: Scheduler = Schedulers.io()
) : PricesRepository {


    override fun prices(market: String): Flowable<Price> {
        return Flowable
            .interval(300, TimeUnit.MILLISECONDS, scheduler)
            .map { generatePrice(market) }
            .distinctUntilChanged()
    }

    private fun generatePrice(marketName: String?): Price =
        Price(marketName, randomProvider.random(), Direction.UNKNOWN)
}