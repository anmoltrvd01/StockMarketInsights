package com.example.stockmarketinsights.repository

import com.example.stockmarketinsights.network.RetrofitInstance
import retrofit2.Response

class StockRepository {

    private val api = RetrofitInstance.api

    suspend fun getQuote(symbol: String): Response<String> {
        return api.getQuote(symbol)
    }

    suspend fun getMultipleQuotes(symbols: List<String>): Map<String, String>? {
        val response = api.getMultipleQuotes(symbols)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getOverview(symbol: String): Response<String> {
        return api.getOverview(symbol)
    }

    suspend fun getMultipleOverviews(symbols: List<String>): Map<String, String>? {
        val response = api.getMultipleOverviews(symbols)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getTimeSeries(symbol: String): Response<String> {
        return api.getTimeSeries(symbol)
    }

    suspend fun getMultipleTimeSeries(symbols: List<String>): Map<String, String>? {
        val response = api.getMultipleTimeSeries(symbols)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun searchSymbol(keywords: String): Response<String> {
        return api.searchSymbol(keywords)
    }

    suspend fun getTopStocks(type: String): Response<List<Map<String, String>>> {
        return api.getTopStocks(type)
    }
}
