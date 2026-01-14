package com.example.stockmarketinsights.utils

import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.network.SearchMatch
import com.example.stockmarketinsights.network.StockItem


fun StockItem.toStockSummaryItem(): StockSummaryItem {
    return StockSummaryItem(
        name = ticker,           // Alpha Vantage limitation
        symbol = ticker,
        price = if (price.isNotBlank()) "$$price" else "--",
        changePercent = change_percentage.ifBlank { "--" }
    )
}
fun SearchMatch.toStockSummaryItem(): StockSummaryItem {
    return StockSummaryItem(
        name = name,
        symbol = symbol,
        price = "--",
        changePercent = "--"
    )
}