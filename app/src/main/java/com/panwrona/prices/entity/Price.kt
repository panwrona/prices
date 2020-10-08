package com.panwrona.prices.entity

data class Price(
    val marketName: String?,
    val value: Int,
    val direction: Direction
)

enum class Direction {
    UP, DOWN, UNKNOWN
}