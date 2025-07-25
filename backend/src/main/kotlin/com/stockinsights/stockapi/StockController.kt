package com.stockinsights.stockapi

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stocks")
class StockController(
    private val alphaVantageService: AlphaVantageService
) {
    @GetMapping("/overview")
    fun getOverview(@RequestParam symbol: String): String {
        return alphaVantageService.getStockOverview(symbol)
    }
}

