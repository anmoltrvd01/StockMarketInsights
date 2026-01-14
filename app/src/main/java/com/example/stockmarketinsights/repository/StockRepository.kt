package com.example.stockmarketinsights.repository

import com.example.stockmarketinsights.BuildConfig
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.dataModel.UiStockItem
import com.example.stockmarketinsights.network.AlphaVantageApiService
import com.example.stockmarketinsights.network.RetrofitInstance
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.utils.ApiRateLimiter
import com.example.stockmarketinsights.utils.toEntity
import com.example.stockmarketinsights.utils.toStockSummaryItem
import com.example.stockmarketinsights.utils.toUi

class StockRepository(
    private val api: AlphaVantageApiService = RetrofitInstance.api,
    private val db: AppDatabase? = null
) {

    private val stockDao = db?.stockDao()
    private val apiKey = BuildConfig.ALPHA_VANTAGE_API_KEY


    // Top Gainers / Losers (Cached)

    suspend fun getTopStocks(type: String): List<StockSummaryItem> {

        // Try cache first
        val cached = stockDao?.getAllStocks().orEmpty()
        if (cached.isNotEmpty()) {
            return cached.map { it.toUi() }
        }

        // Rate limit check
        if (!ApiRateLimiter.canCallApi()) {
            return emptyList()
        }

        ApiRateLimiter.recordCall()

        // API call
        val response = api.getTopGainersAndLosers(apiKey = apiKey)
        val apiStocks = if (type == "gainers") {
            response.top_gainers
        } else {
            response.top_losers
        }

        val uiStocks = apiStocks.map { it.toStockSummaryItem() }

        // Cache result if DB exists
        stockDao?.insertStocks(uiStocks.map { it.toEntity() })

        return uiStocks
    }


    // Stock Search (NO CACHE)

    suspend fun searchSymbol(query: String): List<UiStockItem> {

        if (!ApiRateLimiter.canCallApi()) {
            return emptyList()
        }

        ApiRateLimiter.recordCall()

        val response = api.searchSymbols(
            keywords = query,
            apiKey = apiKey
        )

        return response.bestMatches.map {
            UiStockItem(
                symbol = it.symbol,
                name = it.name
            )
        }
    }
}
