package com.stockinsights.stockapi.controller

import com.stockinsights.stockapi.data.StaticStockList
import com.stockinsights.stockapi.model.CandlePoint
import com.stockinsights.stockapi.model.FinnhubQuoteResponse
import com.stockinsights.stockapi.model.StockOverview
import com.stockinsights.stockapi.model.dp
import com.stockinsights.stockapi.service.FinnhubService
import com.stockinsights.stockapi.service.StockAnalysisService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stocks")
class StockController(
    private val finnhubService: FinnhubService,
    private val stockAnalysisService: StockAnalysisService
) {

    @GetMapping("/quote")
    fun getQuote(@RequestParam symbol: String): String =
        finnhubService.getQuote(symbol)

    @GetMapping("/search")
    fun search(@RequestParam query: String): String =
        finnhubService.searchSymbol(query)

    @GetMapping("/candle")
    fun getCandleData(
        @RequestParam symbol: String,
        @RequestParam resolution: String,
        @RequestParam from: Long,
        @RequestParam to: Long
    ): String = finnhubService.getCandles(symbol, resolution, from, to)

    @GetMapping("/candle/simulated")
    fun getSimulatedCandles(@RequestParam symbol: String): List<CandlePoint> =
        finnhubService.generateSimulatedCandles(symbol)
    @GetMapping("/top-gainers")
    fun getTopGainers(): List<StockOverview> = stockAnalysisService.getTopGainers()

    @GetMapping("/top-losers")
    fun getTopLosers(): List<StockOverview> = stockAnalysisService.getTopLosers()

    @GetMapping("/nifty")
    fun getNifty(): List<StockOverview> = stockAnalysisService.getNiftyStocks()

    @GetMapping("/sensex")
    fun getSensex(): List<StockOverview> = stockAnalysisService.getSensexStocks()


    @GetMapping("/trending")
    fun getTrending(): Map<String, FinnhubQuoteResponse> =
        finnhubService.getBatchQuotes(StaticStockList.trendingStocks)

}
