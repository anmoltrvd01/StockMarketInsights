package com.example.stockmarketinsights.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItem(item: WatchlistEntity)

    @Delete
    suspend fun deleteWatchlistItem(item: WatchlistEntity)

    @Query("SELECT * FROM watchlist_table")
    fun getAllWatchlistItems(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_table WHERE watchlistName = :watchlistName")
    fun getWatchlistItemsByName(watchlistName: String): Flow<List<WatchlistEntity>>

    @Query("SELECT DISTINCT watchlistName FROM watchlist_table")
    fun getAllWatchlistNames(): Flow<List<String>>

    @Query("UPDATE watchlist_table SET watchlistName = :newName WHERE watchlistName = :oldName")
    suspend fun renameWatchlist(oldName: String, newName: String)
}
