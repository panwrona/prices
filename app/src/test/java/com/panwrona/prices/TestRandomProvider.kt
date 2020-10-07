package com.panwrona.prices

import com.panwrona.prices.repository.RandomProvider

class TestRandomProvider: RandomProvider() {

    override fun random() = 1
}