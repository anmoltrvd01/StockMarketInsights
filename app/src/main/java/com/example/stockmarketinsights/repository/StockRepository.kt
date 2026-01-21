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
    private val context: Context,
    private val api: AlphaVantageApiService = RetrofitInstance.api,
    private val db: AppDatabase
) {

    private val stockDao = db.stockDao()
    private val apiKey = BuildConfig.ALPHA_VANTAGE_API_KEY

    suspend fun getTopStocks(type: String): List<StockSummaryItem> {

        println("REPO -> getTopStocks called for type = $type")

        if (!NetworkUtils.isConnected(context)) {
            println("REPO -> OFFLINE MODE")
            val cached = stockDao.getAllStocks()
            println("REPO -> cached size = ${cached.size}")
            return cached.map { it.toUi() }
        }

        if (!ApiRateLimiter.canCallApi()) {
            println("REPO -> RATE LIMITED")
            val cached = stockDao.getAllStocks()
            println("REPO -> cached size = ${cached.size}")
            return cached.map { it.toUi() }
        }

        ApiRateLimiter.recordCall()
        println("REPO -> API CALL STARTED")

        val response = api.getTopGainersAndLosers(apiKey = apiKey)

        val apiStocks =
            if (type.lowercase() == "gainers") response.top_gainers
            else response.top_losers

        println("REPO -> API raw stocks size = ${apiStocks.size}")

        val uiStocks = apiStocks.map { it.toStockSummaryItem() }

        println("REPO -> UI stocks size = ${uiStocks.size}")

        stockDao.insertStocks(uiStocks.map { it.toEntity() })

        return uiStocks
    }

    suspend fun searchSymbol(query: String): List<StockSummaryItem> {
        println("REPO -> searchSymbol called with query = $query")

        if (!NetworkUtils.isConnected(context)) {
            println("REPO -> OFFLINE SEARCH")
            val cached = stockDao.searchStocks("%$query%")
            return cached.map { it.toUi() }
        }

        if (!ApiRateLimiter.canCallApi()) {
            println("REPO -> RATE LIMITED SEARCH")
            return emptyList()
        }

        ApiRateLimiter.recordCall()
        val response = api.searchSymbols(keywords = query, apiKey = apiKey)
        println("REPO -> search results size = ${response.bestMatches.size}")

        return response.bestMatches.map { it.toStockSummaryItem() }
    }

    suspend fun getTopGainers(): List<StockSummaryItem> {
        return getTopStocks("gainers")
    }

    suspend fun getTopLosers(): List<StockSummaryItem> {
        return getTopStocks("losers")
    }

    suspend fun getCompanyOverview(symbol: String): CompanyOverview {
        println("REPO -> getCompanyOverview called for $symbol")
        return CompanyOverview(
            symbol = symbol,
            name = "Company $symbol",
            description = "Description for $symbol",
            sector = "Technology",
            industry = "Software"
        )
    }

    suspend fun getDailyPrices(symbol: String): List<DailySeries> {
        println("REPO -> getDailyPrices called for $symbol")
        return listOf(
            DailySeries(
                date = "2026-01-20",
                open = 100.0,
                high = 105.0,
                low = 98.0,
                close = 102.0,
                volume = 10000
            ),
            DailySeries(
                date = "2026-01-19",
                open = 102.0,
                high = 106.0,
                low = 101.0,
                close = 104.0,
                volume = 12000
            )
        )
    }
}
