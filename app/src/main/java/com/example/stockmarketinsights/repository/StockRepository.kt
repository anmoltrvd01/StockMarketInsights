package com.example.stockmarketinsights.repository

import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.network.RetrofitInstance

class StockRepository {

    private val api = RetrofitInstance.api

    suspend fun getTopGainers(): List<StockSummaryItem> {
        return api.getTopGainers()
    }

    suspend fun getTopLosers(): List<StockSummaryItem> {
        return api.getTopLosers()
    }

    suspend fun searchStocks(query: String): List<StockSummaryItem> {
        return api.searchStocks(query)
    }
}
