package com.example.stockmarketinsights.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlphaVantageApi {

    @GET("query")
    suspend fun getStockPrices(
        @Query("function") function: String = "TIME_SERIES_DAILY",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): Response<JsonObject>


    @GET("query")
    suspend fun getTopGainers(
        @Query("function") function: String = "TOP_GAINERS",
        @Query("apikey") apiKey: String
    ): Response<JsonObject>

    @GET("query")
    suspend fun getTopLosers(
        @Query("function") function: String = "TOP_LOSERS",
        @Query("apikey") apiKey: String
    ): Response<JsonObject>
}
