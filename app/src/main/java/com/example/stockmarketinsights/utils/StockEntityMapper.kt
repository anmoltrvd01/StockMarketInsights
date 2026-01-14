package com.example.stockmarketinsights.utils

import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.roomdb.StockEntity

fun StockEntity.toUi(): StockSummaryItem {
    return StockSummaryItem(
        name = name,
        symbol = symbol,
        price = price,
        changePercent = changePercent
    )
}

fun StockSummaryItem.toEntity(): StockEntity {
    return StockEntity(
        symbol = symbol,
        name = name,
        price = price,
        changePercent = changePercent,
        lastUpdated = System.currentTimeMillis()
    )
}
