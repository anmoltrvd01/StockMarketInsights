package com.example.stockmarketinsights.roomdb

import kotlinx.coroutines.flow.Flow

class WatchlistRepository(
    private val watchlistDao: WatchlistDao,
    private val gainerDao: GainerDao,
    private val loserDao: LoserDao
) {
    // Watchlist
    fun getAllWatchlistItems(): Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlistItems()
    suspend fun insertStock(stock: WatchlistEntity) = watchlistDao.insertStock(stock)
    suspend fun deleteStock(stock: WatchlistEntity) = watchlistDao.deleteStock(stock)

    // Gainers
    fun getAllGainers(): Flow<List<GainerEntity>> = gainerDao.getAll()
    suspend fun cacheGainers(gainers: List<GainerEntity>) {
        gainerDao.clearAll()
        gainerDao.insertAll(gainers)
    }

    // Losers
    fun getAllLosers(): Flow<List<LoserEntity>> = loserDao.getAll()
    suspend fun cacheLosers(losers: List<LoserEntity>) {
        loserDao.clearAll()
        loserDao.insertAll(losers)
    }
}
