package com.panwrona.prices.ui

import com.panwrona.prices.entity.Price

interface MainView {

    fun updatePrice(price: Price)
}