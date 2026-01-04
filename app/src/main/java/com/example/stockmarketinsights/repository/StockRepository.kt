package com.example.stockmarketinsights.repository

import com.example.stockmarketinsights.BuildConfig
import com.example.stockmarketinsights.dataModel.UiStockItem
import com.example.stockmarketinsights.network.AlphaVantageApiService
import com.example.stockmarketinsights.network.RetrofitInstance

class StockRepository {

    private val api: AlphaVantageApiService = RetrofitInstance.api
    private val apiKey = BuildConfig.ALPHA_VANTAGE_API_KEY


    suspend fun searchStocks(query: String): List<UiStockItem> {
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
