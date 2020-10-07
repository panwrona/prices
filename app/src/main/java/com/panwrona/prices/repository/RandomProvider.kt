package com.panwrona.prices.repository

import kotlin.random.Random

open class RandomProvider {

    open fun random() = Random.nextInt(0, Int.MAX_VALUE)
}