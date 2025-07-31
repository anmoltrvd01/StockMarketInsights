package com.stockinsights.stockapi

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stocks")
class StockController(private val alphaVantageService: AlphaVantageService) {

    @GetMapping("/overview")
    fun getOverview(@RequestParam symbol: String): String =
        alphaVantageService.getStockOverview(symbol)

    @GetMapping("/quote")
    fun getQuote(@RequestParam symbol: String): String =
        alphaVantageService.getGlobalQuote(symbol)

    @GetMapping("/search")
    fun search(@RequestParam keywords: String): String =
        alphaVantageService.searchSymbol(keywords)

    @GetMapping("/timeseries")
    fun getTimeSeries(@RequestParam symbol: String): String =
        alphaVantageService.getTimeSeriesDaily(symbol)

    @GetMapping("/top")
    fun getTopStocks(@RequestParam type: String): List<Map<String, String>> =
        alphaVantageService.getTopStocks(type)

    @GetMapping("/quotes")
    fun getMultipleQuotes(@RequestParam symbols: List<String>): Map<String, String> =
        alphaVantageService.getMultipleQuotes(symbols)

    @GetMapping("/overviews")
    fun getMultipleOverviews(@RequestParam symbols: List<String>): Map<String, String> =
        alphaVantageService.getMultipleOverviews(symbols)

    @GetMapping("/timeseries/batch")
    fun getMultipleTimeSeries(@RequestParam symbols: List<String>): Map<String, String> =
        alphaVantageService.getMultipleTimeSeries(symbols)


}
