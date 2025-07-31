package com.stockinsights.stockapi.service

import com.stockinsights.stockapi.model.CandlePoint
import com.stockinsights.stockapi.model.FinnhubQuoteResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class FinnhubService(private val restTemplate: RestTemplate) {

    @Value("\${finnhub.api.key}")
    private lateinit var apiKey: String
    fun apiKey(): String = apiKey

    private val baseUrl = "https://finnhub.io/api/v1"

    fun getQuote(symbol: String): String {
        val url = "$baseUrl/quote?symbol=$symbol&token=$apiKey"
        return restTemplate.getForObject(url, String::class.java) ?: "Error fetching quote"
    }

    fun searchSymbol(query: String): String {
        val url = "$baseUrl/search?q=$query&token=$apiKey"
        return restTemplate.getForObject(url, String::class.java) ?: "Error fetching search"
    }

    fun getCandles(symbol: String, resolution: String, from: Long, to: Long): String {
        val url = "$baseUrl/stock/candle?symbol=$symbol&resolution=$resolution&from=$from&to=$to&token=$apiKey"
        return restTemplate.getForObject(url, String::class.java) ?: "Error fetching candles"
    }

    fun generateSimulatedCandles(symbol: String): List<CandlePoint> {
        val url = "$baseUrl/quote?symbol=$symbol&token=$apiKey"
        val response = restTemplate.getForObject(url, FinnhubQuoteResponse::class.java)
            ?: throw RuntimeException("Failed to fetch quote")

        val now = System.currentTimeMillis() / 1000
        val oneDay = 86400L

        return (0..6).map { i ->
            val timestamp = now - (6 - i) * oneDay
            val randomFactor = (0.98 + Math.random() * 0.04)


            CandlePoint(
                time = timestamp,
                open = response.o * randomFactor,
                high = response.h * randomFactor,
                low = response.l * randomFactor,
                close = response.c * randomFactor
            )
        }
    }
    fun getBatchQuotes(symbols: List<String>): Map<String, FinnhubQuoteResponse> {
        return symbols.associateWith { symbol ->
            val url = "$baseUrl/quote?symbol=$symbol&token=$apiKey"
            restTemplate.getForObject(url, FinnhubQuoteResponse::class.java)
                ?: FinnhubQuoteResponse(0.0, 0.0, 0.0, 0.0, 0.0)
        }
    }

}
