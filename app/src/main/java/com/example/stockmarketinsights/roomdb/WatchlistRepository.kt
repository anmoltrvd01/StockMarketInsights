package com.example.stockmarketinsights.roomdb

import kotlinx.coroutines.flow.Flow

class WatchlistRepository(private val dao: WatchlistDao) {

    suspend fun insertWatchlist(watchlist: WatchlistEntity) {
        dao.insertWatchlistItem(watchlist)
    }

    fun getAllWatchlists(): Flow<List<WatchlistEntity>> {
        return dao.getAllWatchlistItems()
    }

    suspend fun deleteWatchlist(watchlist: WatchlistEntity) {
        dao.deleteWatchlistItem(watchlist)
    }

    suspend fun renameWatchlist(oldName: String, newName: String) {
        dao.renameWatchlist(oldName, newName)
    }
}
