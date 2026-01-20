package com.example.stockmarketinsights.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.roomdb.WatchlistRepository
import com.example.stockmarketinsights.screensUi.*
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModelFactory
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
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = WatchlistRepository(db.watchlistDao())
    val watchlistViewModel: WatchlistViewModel =
        viewModel(factory = WatchlistViewModelFactory(repository))

    NavHost(
        navController = navController,
        startDestination = Screen.Explore.route,
        modifier = modifier
    ) {

        //  EXPLORE
        composable(Screen.Explore.route) {
            ExploreScreen(
                modifier = modifier,
                onStockClick = { symbol ->
                    // navigate only with symbol for now
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        // DETAILS
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

            val stock = StockSummaryItem(
                name = name,
                symbol = symbol,
                price = price,
                changePercent = changePercent
            )

            DetailsScreen(
                stock = stock,
                navController = navController
            )
        }

        // WATCHLIST
        composable(Screen.Watchlist.route) {
            WatchlistScreen(
                viewModel = watchlistViewModel,
                onStockClick = { stock ->
                    navController.navigate(
                        Screen.Details.withArgs(
                            stock.name,
                            stock.symbol,
                            stock.price,
                            stock.changePercent
                        )
                    )
                },
                onWatchlistClick = { name ->
                    navController.navigate(
                        Screen.WatchlistDetail.withArgs(name)
                    )
                },
                onSearchClick = {
                    navController.navigate("watchlistSearch")
                }
            )
        }

        composable(
            route = "watchlistDetail/{watchlistName}",
            arguments = listOf(
                navArgument("watchlistName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val watchlistName =
                safeDecode(backStackEntry.arguments?.getString("watchlistName"))

            WatchlistDetailScreen(
                watchlistName = watchlistName,
                navController = navController,
                onStockClick = { stock ->
                    navController.navigate(
                        Screen.Details.withArgs(
                            stock.name,
                            stock.symbol,
                            stock.price,
                            stock.changePercent
                        )
                    )
                }
            )
        }

        composable("watchlistSearch") {
            WatchlistSearchScreen(
                viewModel = watchlistViewModel,
                onBack = { navController.popBackStack() },
                onWatchlistClick = { name ->
                    navController.navigate(
                        Screen.WatchlistDetail.withArgs(name)
                    )
                }
            )
        }

        // GLOBAL SEARCH
        composable(Screen.Search.route) {
            SearchAllStocksScreen(
                onStockClick = { stock ->
                    navController.navigate(
                        Screen.Details.withArgs(
                            stock.name,
                            stock.symbol,
                            stock.price,
                            stock.changePercent
                        )
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
