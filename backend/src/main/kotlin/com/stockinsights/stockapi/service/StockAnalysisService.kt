package com.stockinsights.stockapi.service

import com.stockinsights.stockapi.model.FinnhubQuoteResponse
import com.stockinsights.stockapi.model.StockOverview
import com.stockinsights.stockapi.model.dp
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class StockAnalysisService(
    private val restTemplate: RestTemplate,
    private val finnhubService: FinnhubService
) {
    private val trackedSymbols = listOf(
        "AAPL", "MSFT", "TSLA", "GOOGL", "AMZN",
        "INFY", "RELIANCE.NS", "TCS.NS", "HDFCBANK.NS"
    )

    fun getTopGainers(): List<StockOverview> = fetchAndSortByDp(descending = true)
    fun getTopLosers(): List<StockOverview> = fetchAndSortByDp(descending = false)

    fun getNiftyStocks(): List<StockOverview> = fetchQuotes(
        listOf("RELIANCE.NS", "TCS.NS", "INFY", "ITC.NS", "ICICIBANK.NS")
    )

    fun getSensexStocks(): List<StockOverview> = fetchQuotes(
        listOf("RELIANCE.NS", "TCS.NS", "HDFCBANK.NS", "BAJAJFINSV.NS", "ASIANPAINT.NS")
    )

    private fun fetchAndSortByDp(descending: Boolean): List<StockOverview> =
        fetchQuotes(trackedSymbols).sortedBy { it.dp }.let {
            if (descending) it.reversed().take(5) else it.take(5)
        }

    private fun fetchQuotes(symbols: List<String>): List<StockOverview> =
        symbols.mapNotNull { symbol ->
            runCatching {
                val url = "https://finnhub.io/api/v1/quote?symbol=$symbol&token=${finnhubService.apiKey()}"
                val response = restTemplate.getForObject(url, FinnhubQuoteResponse::class.java)
                response?.let {
                    StockOverview(symbol, it.c, it.dp())
                }
            }.getOrElse {
                println("Failed to fetch $symbol: ${it.message}")
                null
            }
        }
}
