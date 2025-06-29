package com.example.stockmarketinsights.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "losers")
data class LoserEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val price: Float,
    val changePercent: Float
)
