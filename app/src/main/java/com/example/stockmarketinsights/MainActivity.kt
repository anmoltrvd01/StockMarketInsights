package com.example.stockmarketinsights

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.screensUi.*
import com.example.stockmarketinsights.ui.theme.StockMarketInsightsTheme
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockMarketInsightsTheme {
                if (!LocalInspectionMode.current) {
                    MainScreen()
                } else {
                    Text("Preview not supported")
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "explore",
                    onClick = { navController.navigate("explore") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Explore") },
                    label = { Text("Explore") }
                )
                NavigationBarItem(
                    selected = currentRoute == "watchlist",
                    onClick = { navController.navigate("watchlist") },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Watchlist") },
                    label = { Text("Watchlist") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "explore",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("explore") {
                ExploreScreen(
                    onViewAllGainersClick = { navController.navigate("viewall") },
                    onViewAllLosersClick = { navController.navigate("viewall") },
                    onStockClick = { stock ->
                        val encodedName = URLEncoder.encode(stock.name, "UTF-8")
                        val encodedSymbol = URLEncoder.encode(stock.symbol, "UTF-8")
                        val encodedPrice = URLEncoder.encode(stock.price, "UTF-8")
                        val encodedChange = URLEncoder.encode(stock.changePercent, "UTF-8")

                        navController.navigate(
                            "details/$encodedName/$encodedSymbol/$encodedPrice/$encodedChange"
                        )
                    }
                )
            }

            composable("watchlist") {
                WatchlistScreen(
                    onWatchlistClick = { name ->
                        navController.navigate("watchlistDetail/${name}")
                    }
                )
            }

            composable("viewall") {
                ViewAllScreen()
            }

            composable(
                route = "watchlistDetail/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType })
            ) { backStackEntry ->
                val watchlistName = backStackEntry.arguments?.getString("name") ?: "Watchlist"
                WatchlistDetailScreen(watchlistName = watchlistName)
            }

            composable(
                route = "details/{name}/{symbol}/{price}/{changePercent}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("symbol") { type = NavType.StringType },
                    navArgument("price") { type = NavType.StringType },
                    navArgument("changePercent") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                fun safeDecode(value: String?): String {
                    return try {
                        URLDecoder.decode(value ?: "", "UTF-8")
                    } catch (e: IllegalArgumentException) {
                        value ?: "" // Fallback to raw
                    }
                }

                val name = safeDecode(backStackEntry.arguments?.getString("name"))
                val symbol = safeDecode(backStackEntry.arguments?.getString("symbol"))
                val price = safeDecode(backStackEntry.arguments?.getString("price"))
                val changePercent = safeDecode(backStackEntry.arguments?.getString("changePercent"))

                val stock = StockSummaryItem(
                    name = name,
                    symbol = symbol,
                    price = price,
                    changePercent = changePercent
                )

                DetailsScreen(stock = stock, navController = navController)
            }
        }
    }
}
