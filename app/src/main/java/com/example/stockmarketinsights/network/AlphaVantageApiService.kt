package com.example.stockmarketinsights.network

import retrofit2.http.GET
import retrofit2.http.Query

// --- Top Gainers & Losers ---
data class TopGainersLosersResponse(
    val top_gainers: List<StockItem>,
    val top_losers: List<StockItem>
)

// --- Generic Stock Item ---
data class StockItem(
    val ticker: String,
    val price: String,
    val change_amount: String,
    val change_percentage: String,
    val volume: String
)

// --- Company Overview ---
data class CompanyOverviewResponse(
    val Symbol: String,
    val Name: String,
    val Sector: String,
    val MarketCapitalization: String,
    val Description: String
)

// --- Search Response ---
data class SymbolSearchResponse(
    val bestMatches: List<SearchMatch>
)

data class SearchMatch(
    val symbol: String,
    val name: String,
    val region: String,
    val currency: String
)

interface AlphaVantageApiService {

    // Alpha Intelligence – Top Gainers & Losers
    @GET("query")
    suspend fun getTopGainersAndLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String
    ): TopGainersLosersResponse

    // Ticker Search
    @GET("query")
    suspend fun searchSymbols(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String
    ): SymbolSearchResponse

    // Company Overview
    @GET("query")
    suspend fun getCompanyOverview(
        @Query("function") function: String = "OVERVIEW",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): CompanyOverviewResponse
}
