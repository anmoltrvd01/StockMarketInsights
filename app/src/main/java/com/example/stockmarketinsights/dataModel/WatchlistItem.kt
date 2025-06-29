package com.example.stockmarketinsights.dataModel

data class WatchlistItem(
    val symbol: String,
    val name: String,
    val price: Float,
    val changePercent: Float
)
