package com.panwrona.prices.repository

import com.panwrona.prices.entity.Direction
import com.panwrona.prices.entity.Price
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class PricesRepositoryImpl(
    private val randomProvider: RandomProvider,
    private val scheduler: Scheduler
): PricesRepository {

    private val isEmissionAllowed = AtomicBoolean(false)

    override fun prices(marketName: String): Flowable<Price> {
        return Flowable.interval(300, TimeUnit.MILLISECONDS, scheduler)
            .filter { isEmissionAllowed.get() }
            .map { generatePrice(marketName) }
            .scan(Price(marketName, randomProvider.random(), Direction.UNKNOWN), { old, new ->
                when {
                    old.value < new.value -> {
                        new.copy(direction = Direction.UP)
                    }
                    old.value > new.value -> {
                        new.copy(direction = Direction.DOWN)
                    }
                    else -> {
                        new
                    }
                }
            })
            .distinctUntilChanged()
    }

    fun stopEmission() {
        isEmissionAllowed.set(false)
    }

    fun startEmission() {
        isEmissionAllowed.set(true)
    }

    private fun generatePrice(marketName: String): Price = Price(marketName, randomProvider.random(), Direction.UNKNOWN)
}