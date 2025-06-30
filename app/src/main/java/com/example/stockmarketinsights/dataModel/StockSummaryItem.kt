package com.example.stockmarketinsights.dataModel

import java.io.Serializable

data class StockSummaryItem(
    val name: String,
    val symbol: String,
    val price: String,
    val changePercent: String
) : Serializable

data class StockDetail(
    val name: String,
    val symbol: String,
    val price: String,
    val marketCap: String
) : Serializable
