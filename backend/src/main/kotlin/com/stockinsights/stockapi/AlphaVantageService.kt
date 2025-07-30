package com.stockinsights.stockapi

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AlphaVantageService(private val restTemplate: RestTemplate) {

    @Value("\${alpha.api.key}")
    private lateinit var apiKey: String

    private val baseUrl = "https://www.alphavantage.co/query"

    private fun fetchUrl(url: String): String {
        return try {
            restTemplate.getForObject(url, String::class.java) ?: "Error: Empty response"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    @Cacheable("stockOverview")
    fun getStockOverview(symbol: String): String {
        val url = "$baseUrl?function=OVERVIEW&symbol=$symbol&apikey=$apiKey"
        return fetchUrl(url)
    }

    @Cacheable("stockQuote")
    fun getGlobalQuote(symbol: String): String {
        val url = "$baseUrl?function=GLOBAL_QUOTE&symbol=$symbol&apikey=$apiKey"
        return fetchUrl(url)
    }

    @Cacheable("stockSearch")
    fun searchSymbol(keywords: String): String {
        val url = "$baseUrl?function=SYMBOL_SEARCH&keywords=$keywords&apikey=$apiKey"
        return fetchUrl(url)
    }

    @Cacheable("historicalData")
    fun getTimeSeriesDaily(symbol: String): String {
        val url = "$baseUrl?function=TIME_SERIES_DAILY_ADJUSTED&symbol=$symbol&outputsize=compact&apikey=$apiKey"
        return fetchUrl(url)
    }

    // Mock data only — optional: replace later with real logic
    fun getTopStocks(type: String): List<Map<String, String>> {
        return when (type.lowercase()) {
            "gainers" -> listOf(
                mapOf("symbol" to "TSLA", "name" to "Tesla", "price" to "254.32", "changePercent" to "+5.3%"),
                mapOf("symbol" to "NVDA", "name" to "Nvidia", "price" to "430.89", "changePercent" to "+4.7%"),
                mapOf("symbol" to "META", "name" to "Meta", "price" to "298.56", "changePercent" to "+3.6%")
            )
            "losers" -> listOf(
                mapOf("symbol" to "AMZN", "name" to "Amazon", "price" to "118.45", "changePercent" to "-4.6%"),
                mapOf("symbol" to "INTC", "name" to "Intel", "price" to "34.12", "changePercent" to "-2.5%"),
                mapOf("symbol" to "NFLX", "name" to "Netflix", "price" to "410.23", "changePercent" to "-1.9%")
            )
            else -> emptyList()
        }
    }

    // For batch quote/overview/timeSeries
    fun getMultipleQuotes(symbols: List<String>): Map<String, String> =
        symbols.associateWith {
            try {
                getGlobalQuote(it)
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }

    fun getMultipleOverviews(symbols: List<String>): Map<String, String> =
        symbols.associateWith {
            try {
                getStockOverview(it)
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }

    fun getMultipleTimeSeries(symbols: List<String>): Map<String, String> =
        symbols.associateWith {
            try {
                getTimeSeriesDaily(it)
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }

}
