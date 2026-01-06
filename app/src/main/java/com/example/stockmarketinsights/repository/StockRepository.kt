package com.example.stockmarketinsights.repository

import com.example.stockmarketinsights.BuildConfig
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.network.RetrofitInstance
import com.example.stockmarketinsights.utils.toStockSummaryItem

class StockRepository {

    private val api = RetrofitInstance.api
    private val apiKey = BuildConfig.ALPHA_VANTAGE_API_KEY

    suspend fun getTopStocks(type: String): List<StockSummaryItem> {
        val response = api.getTopGainersAndLosers(apiKey = apiKey)

        val stocks = if (type == "gainers") {
            response.top_gainers
        } else {
            response.top_losers
        }

        return stocks.map { it.toStockSummaryItem() }
    }

    suspend fun searchStocks(query: String): List<StockSummaryItem> {
        val response = api.searchSymbols(
            keywords = query,
            apiKey = apiKey
        )

        return response.bestMatches.map {
            StockSummaryItem(
                name = it.name,
                symbol = it.symbol,
                price = "--",
                changePercent = "--"
            )
        }
    }
}
