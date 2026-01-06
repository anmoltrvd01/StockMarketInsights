package com.example.stockmarketinsights.dataModel

import java.io.Serializable


data class MarketIndices(
    val nifty: String,
    val sensex: String
)

data class UiStockItem(
    val symbol: String,
    val name: String,
    val price: String = "--",
    val changePercent: String = "--",
    val volume: String = "--"
)


data class StockDetail(
    val name: String,
    val symbol: String,
    val price: String,
    val marketCap: String
) : Serializable
