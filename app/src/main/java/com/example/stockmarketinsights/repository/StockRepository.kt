package com.example.stockmarketinsights.repository

import android.content.Context
import com.example.stockmarketinsights.BuildConfig
import com.example.stockmarketinsights.dataModel.CompanyOverview
import com.example.stockmarketinsights.dataModel.DailySeries
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

    suspend fun getTopGainers(): List<StockSummaryItem> {
        return getTopStocks("gainers")
    }

    suspend fun getTopLosers(): List<StockSummaryItem> {
        return getTopStocks("losers")
    }

    suspend fun getCompanyOverview(symbol: String): CompanyOverview {
        // TODO: Replace with actual API call later
        return CompanyOverview(
            symbol = symbol,
            name = "Company $symbol",
            description = "Description for $symbol",
            sector = "Technology",
            industry = "Software"
        )
    }

    suspend fun getDailyPrices(symbol: String): List<DailySeries> {
        // TODO: Replace with actual API call later
        return listOf(
            DailySeries(date = "2026-01-20", open = 100.0, high = 105.0, low = 98.0, close = 102.0, volume = 10000),
            DailySeries(date = "2026-01-19", open = 102.0, high = 106.0, low = 101.0, close = 104.0, volume = 12000)
        )
    }

}
