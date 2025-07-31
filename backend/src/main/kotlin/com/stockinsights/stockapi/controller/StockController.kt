package com.stockinsights.stockapi.controller

import com.stockinsights.stockapi.data.StaticStockList
import com.stockinsights.stockapi.model.CandlePoint
import com.stockinsights.stockapi.model.FinnhubQuoteResponse
import com.stockinsights.stockapi.service.FinnhubService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stocks")
class StockController(
    private val finnhubService: FinnhubService
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



}
