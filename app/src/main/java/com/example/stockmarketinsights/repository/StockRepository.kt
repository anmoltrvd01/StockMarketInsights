package com.example.stockmarketinsights.repository

import com.example.stockmarketinsights.BuildConfig
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.network.AlphaVantageApiService
import com.example.stockmarketinsights.network.RetrofitInstance
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.utils.ApiRateLimiter
import com.example.stockmarketinsights.utils.toEntity
import com.example.stockmarketinsights.utils.toStockSummaryItem
import com.example.stockmarketinsights.utils.toUi

class StockRepository(
    private val api: AlphaVantageApiService = RetrofitInstance.api,
    private val db: AppDatabase
) {

    private val stockDao = db.stockDao()
    private val apiKey = BuildConfig.ALPHA_VANTAGE_API_KEY

    suspend fun getTopStocks(type: String): List<StockSummaryItem> {

        //Always try cache first
        val cached = stockDao.getAllStocks()
        if (cached.isNotEmpty()) {
            return cached.map { it.toUi() }
        }

        // Enforce rate limit
        if (!ApiRateLimiter.canCallApi()) {
            return emptyList() // safe fallback
        }

        //API call
        ApiRateLimiter.recordCall()

        val response = api.getTopGainersAndLosers(apiKey = apiKey)
        val apiStocks = if (type == "gainers") {
            response.top_gainers
        } else {
            response.top_losers
        }

        val uiStocks = apiStocks.map { it.toStockSummaryItem() }

        // Save to cache
        stockDao.insertStocks(uiStocks.map { it.toEntity() })

        return uiStocks
    }
}

