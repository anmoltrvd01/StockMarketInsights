package com.example.stockmarketinsights.dataModel

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class StockSummaryItem(
    val name: String,
    val symbol: String,
    val price: String,
    val changePercent: String
) : Serializable

data class CompanyOverview(

    @SerializedName("Symbol")
    val symbol: String?,

    @SerializedName("Name")
    val name: String?,

    @SerializedName("Description")
    val description: String?,

    @SerializedName("Sector")
    val sector: String?,

    @SerializedName("Industry")
    val industry: String?,

    @SerializedName("MarketCapitalization")
    val marketCap: String?,

    @SerializedName("PERatio")
    val peRatio: String?,

    @SerializedName("DividendYield")
    val dividendYield: String?,

    @SerializedName("52WeekHigh")
    val week52High: String?,

    @SerializedName("52WeekLow")
    val week52Low: String?,

    @SerializedName("Country")
    val country: String?,

    @SerializedName("Exchange")
    val exchange: String?
)


data class DailySeries(
    val date: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Int
)