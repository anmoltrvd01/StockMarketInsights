package com.example.stockmarketinsights.repository

import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.dataModel.MarketIndices
import com.example.stockmarketinsights.network.RetrofitInstance
import retrofit2.Response

class StockRepository {

    private val api = RetrofitInstance.api

    // Top Gainers/Losers
    suspend fun getTopStocks(type: String): Response<List<Map<String, String>>> {
        return if (type == "gainers") {
            api.getTopGainers()
        } else {
            api.getTopLosers()
        }
    }

    // Search API
    suspend fun searchSymbol(query: String): Response<String> {
        return api.searchStocks(query)
    }

    // Market indices (Nifty & Sensex)
    suspend fun getMarketIndices(): Response<MarketIndices> {
        return api.getMarketIndices()
    }
}
