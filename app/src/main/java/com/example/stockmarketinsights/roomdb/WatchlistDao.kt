package com.example.stockmarketinsights.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist_table")
    fun getAllWatchlistItems(): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: WatchlistEntity)

    @Delete
    suspend fun deleteStock(stock: WatchlistEntity)
}
