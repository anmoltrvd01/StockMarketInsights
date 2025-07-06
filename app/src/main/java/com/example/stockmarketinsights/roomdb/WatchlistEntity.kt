package com.example.stockmarketinsights.roomdb


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_table")
data class WatchlistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val watchlistName: String,
    val stockName: String,
    val stockSymbol: String
)
