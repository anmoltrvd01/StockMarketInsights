package com.example.stockmarketinsights.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.screensUi.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun safeDecode(input: String?): String {
    return try {
        URLDecoder.decode(input ?: "", StandardCharsets.UTF_8.toString())
    } catch (e: Exception) {
        input ?: ""
    }
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.Explore.route, modifier = modifier) {

        composable(Screen.Explore.route) {
            ExploreScreen(
                onViewAllGainersClick = { navController.navigate(Screen.ViewAll.route) },
                onViewAllLosersClick = { navController.navigate(Screen.ViewAll.route) },
                onStockClick = { stock ->
                    navController.navigate(Screen.Details.withArgs(stock.name, stock.symbol, stock.price, stock.changePercent))
                }
            )
        }

        composable(Screen.ViewAll.route) {
            ViewAllScreen(
                onStockClick = { stock ->
                    navController.navigate(Screen.Details.withArgs(stock.name, stock.symbol, stock.price, stock.changePercent))
                }
            )
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
            val name = safeDecode(backStackEntry.arguments?.getString("name"))
            val symbol = safeDecode(backStackEntry.arguments?.getString("symbol"))
            val price = safeDecode(backStackEntry.arguments?.getString("price"))
            val changePercent = safeDecode(backStackEntry.arguments?.getString("changePercent"))

            val stock = StockSummaryItem(name, symbol, price, changePercent)
            DetailsScreen(stock = stock, navController = navController)
        }

        composable(Screen.Product.route) {
            ProductScreen()
        }

        composable(Screen.Watchlist.route) {
            WatchlistScreen(
                onStockClick = { stock ->
                    navController.navigate(Screen.Details.withArgs(stock.name, stock.symbol, stock.price, stock.changePercent))
                },
                onWatchlistClick = { name ->
                    navController.navigate(Screen.WatchlistDetail.withArgs(name))
                }
            )
        }

        composable(
            route = "watchlistDetail/{watchlistName}",
            arguments = listOf(navArgument("watchlistName") { type = NavType.StringType })
        ) { backStackEntry ->
            val watchlistName = safeDecode(backStackEntry.arguments?.getString("watchlistName"))
            WatchlistDetailScreen(
                watchlistName = watchlistName,
                onStockClick = { stock ->
                    navController.navigate(Screen.Details.withArgs(stock.name, stock.symbol, stock.price, stock.changePercent))
                }
            )
        }
    }
}
