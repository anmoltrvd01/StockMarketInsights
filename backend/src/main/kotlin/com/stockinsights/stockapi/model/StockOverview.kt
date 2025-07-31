package com.stockinsights.stockapi.model

data class StockOverview(
    val symbol: String,
    val currentPrice: Double,
    val dp: Double // % change
)
