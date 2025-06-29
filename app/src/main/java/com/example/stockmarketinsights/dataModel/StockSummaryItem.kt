package com.example.stockmarketinsights.dataModel


data class StockSummaryItem(
    val symbol: String,
    val name: String="",
    val price: Float,
    val changePercent: Float
)
