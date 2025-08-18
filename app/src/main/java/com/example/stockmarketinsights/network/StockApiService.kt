package com.example.stockmarketinsights.network

import com.example.stockmarketinsights.dataModel.StockSummaryItem
import retrofit2.http.GET
import retrofit2.http.Query

data class MarketIndicesResponse(
    val nifty: String,
    val sensex: String
)

interface StockApiService {

    @GET("market/indices")
    suspend fun getMarketIndices(): MarketIndicesResponse

    // Backend: /api/stocks/top/gainers
    @GET("api/stocks/top/gainers")
    suspend fun getTopGainers(): List<StockSummaryItem>

    // Backend: /api/stocks/top/losers
    @GET("api/stocks/top/losers")
    suspend fun getTopLosers(): List<StockSummaryItem>

    // Backend: /api/stocks/search?query=TSLA
    @GET("api/stocks/search")
    suspend fun searchStocks(@Query("query") query: String): List<StockSummaryItem>
}
