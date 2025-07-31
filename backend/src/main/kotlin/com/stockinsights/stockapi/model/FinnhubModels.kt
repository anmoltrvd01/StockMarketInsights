package com.stockinsights.stockapi.model

data class FinnhubQuoteResponse(
    val c: Double, // current price
    val o: Double, // open price
    val h: Double, // high price
    val l: Double, // low price
    val pc: Double // previous close
)

data class CandlePoint(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
)

fun FinnhubQuoteResponse.dp(): Double =
    if (pc != 0.0) ((c - pc) / pc) * 100 else 0.0
