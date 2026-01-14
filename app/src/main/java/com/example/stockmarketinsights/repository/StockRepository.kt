package com.example.stockmarketinsights.repository

import android.content.Context
import com.example.stockmarketinsights.BuildConfig
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.network.AlphaVantageApiService
import com.example.stockmarketinsights.network.RetrofitInstance
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.utils.ApiRateLimiter
import com.example.stockmarketinsights.utils.NetworkUtils
import com.example.stockmarketinsights.utils.toEntity
import com.example.stockmarketinsights.utils.toStockSummaryItem
import com.example.stockmarketinsights.utils.toUi

class StockRepository(
    private val context: Context, // needed for NetworkUtils
    private val api: AlphaVantageApiService = RetrofitInstance.api,
    private val db: AppDatabase
) {

    private val stockDao = db.stockDao()
    private val apiKey = BuildConfig.ALPHA_VANTAGE_API_KEY

    /**
     * Fetch top gainers or losers.
     * Returns cached data if offline or API rate limit exceeded.
     */
    suspend fun getTopStocks(type: String): List<StockSummaryItem> {

        // Check offline first
        if (!NetworkUtils.isConnected(context)) {
            val cached = stockDao.getAllStocks()
            if (cached.isNotEmpty()) {
                return cached.map { it.toUi() }
            } else {
                return emptyList() // No network & no cache
            }
        }

        //  Check API rate limiter
        if (!ApiRateLimiter.canCallApi()) {
            // Return cached data if available
            val cached = stockDao.getAllStocks()
            if (cached.isNotEmpty()) return cached.map { it.toUi() }
            return emptyList() // No cache and cannot call API
        }

        //  Make API call
        ApiRateLimiter.recordCall()
        val response = api.getTopGainersAndLosers(apiKey = apiKey)
        val apiStocks = if (type.lowercase() == "gainers") response.top_gainers
        else response.top_losers

        val uiStocks = apiStocks.map { it.toStockSummaryItem() }

        // Save to cache
        stockDao.insertStocks(uiStocks.map { it.toEntity() })

        return uiStocks
    }

    suspend fun searchSymbol(query: String): List<StockSummaryItem> {
        // Offline: search cache
        if (!NetworkUtils.isConnected(context)) {
            val cached = stockDao.searchStocks("%$query%")
            return cached.map { it.toUi() }
        }

        // Rate limiter check
        if (!ApiRateLimiter.canCallApi()) return emptyList()

        ApiRateLimiter.recordCall()
        val response = api.searchSymbols(keywords = query, apiKey = apiKey)
        return response.bestMatches.map { it.toStockSummaryItem() }
    }

}
