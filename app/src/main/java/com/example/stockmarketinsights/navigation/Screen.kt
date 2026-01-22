package com.example.stockmarketinsights.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Explore : Screen("explore")
    object ViewAll : Screen("viewall/{category}")
    object Watchlist : Screen("watchlist")
    object Details : Screen("details/{name}/{symbol}/{price}/{changePercent}")
    object WatchlistDetail : Screen("watchlistDetail/{watchlistName}")
    object Search : Screen("search")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route.substringBefore("/")) // base route
            args.forEach { arg ->
                append("/${URLEncoder.encode(arg, StandardCharsets.UTF_8.toString())}")
            }
        }
    }
}
