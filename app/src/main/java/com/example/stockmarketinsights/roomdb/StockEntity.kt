package com.example.stockmarketinsights.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val price: String,
    val changePercent: String,
    val lastUpdated: Long
)
