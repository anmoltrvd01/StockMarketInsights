package com.example.stockmarketinsights.dataModel

import java.io.Serializable

data class StockSummaryItem(
    val name: String,
    val symbol: String,
    val price: String,
    val changePercent: String
) : Serializable

data class CompanyOverview(
    val symbol: String,
    val name: String,
    val description: String,
    val sector: String,
    val industry: String
)

data class DailySeries(
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Int
)