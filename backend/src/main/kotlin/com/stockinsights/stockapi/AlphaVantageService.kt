// File: AlphaVantageService.kt
package com.stockinsights.stockapi

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AlphaVantageService(
    private val restTemplate: RestTemplate
) {
    @Value("\${alpha.api.key}")
    private lateinit var apiKey: String

    private val baseUrl = "https://www.alphavantage.co/query"

    @Cacheable("stockSummary")
    fun getStockOverview(symbol: String): String {
        val url = "$baseUrl?function=OVERVIEW&symbol=$symbol&apikey=$apiKey"
        return restTemplate.getForObject(url, String::class.java) ?: "Error fetching data"
    }
}

