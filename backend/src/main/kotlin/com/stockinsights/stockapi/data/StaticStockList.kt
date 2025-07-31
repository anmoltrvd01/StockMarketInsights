package com.stockinsights.stockapi.data

object StaticStockList {
    val allStocks = listOf(
        "AAPL", "TSLA", "GOOGL", "MSFT", "AMZN", "META",
        "RELIANCE.NS", "TCS.NS", "INFY", "HDFCBANK.NS",
        "ITC.NS", "ICICIBANK.NS", "SBIN.NS", "WIPRO.NS", "BAJFINANCE.NS"
    )

    val niftyStocks = listOf(
        "RELIANCE.NS", "TCS.NS", "INFY", "HDFCBANK.NS", "ICICIBANK.NS",
        "ITC.NS", "SBIN.NS", "WIPRO.NS", "BAJFINANCE.NS", "LT.NS"
    )

    val sensexStocks = listOf(
        "RELIANCE.NS", "TCS.NS", "INFY", "HDFCBANK.NS", "ICICIBANK.NS",
        "ITC.NS", "SBIN.NS", "WIPRO.NS", "BAJAJFINSV.NS", "ASIANPAINT.NS"
    )

    val trendingStocks = listOf(
        "AAPL", "TSLA", "GOOGL", "MSFT", "AMZN"
    )
}
