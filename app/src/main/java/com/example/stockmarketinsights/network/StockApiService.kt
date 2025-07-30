package com.example.stockmarketinsights.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockApiService {

    @GET("api/stocks/quote")
    suspend fun getQuote(@Query("symbol") symbol: String): Response<String>

    @GET("api/stocks/quotes")
    suspend fun getMultipleQuotes(@Query("symbols") symbols: List<String>): Response<Map<String, String>>

    @GET("api/stocks/overview")
    suspend fun getOverview(@Query("symbol") symbol: String): Response<String>

    @GET("api/stocks/overviews")
    suspend fun getMultipleOverviews(@Query("symbols") symbols: List<String>): Response<Map<String, String>>

    @GET("api/stocks/timeseries")
    suspend fun getTimeSeries(@Query("symbol") symbol: String): Response<String>

    @GET("api/stocks/timeseries/batch")
    suspend fun getMultipleTimeSeries(@Query("symbols") symbols: List<String>): Response<Map<String, String>>

    @GET("api/stocks/search")
    suspend fun searchSymbol(@Query("keywords") query: String): Response<String>

    @GET("api/stocks/top")
    suspend fun getTopStocks(@Query("type") type: String): Response<List<Map<String, String>>>
}
