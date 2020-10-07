package com.panwrona.prices.repository

import com.panwrona.prices.entity.Price
import io.reactivex.rxjava3.core.Flowable

interface PricesRepository {
    fun prices(marketName: String): Flowable<Price>
}