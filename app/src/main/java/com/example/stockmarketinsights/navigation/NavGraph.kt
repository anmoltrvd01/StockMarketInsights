package com.example.stockmarketinsights.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.stockmarketinsights.dataModel.StockSummaryItem
import com.example.stockmarketinsights.repository.StockRepository
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.roomdb.WatchlistRepository
import com.example.stockmarketinsights.screensUi.DetailsScreen
import com.example.stockmarketinsights.screensUi.ExploreScreen
import com.example.stockmarketinsights.screensUi.SearchAllStocksScreen
import com.example.stockmarketinsights.screensUi.ViewAllScreen
import com.example.stockmarketinsights.screensUi.WatchlistDetailScreen
import com.example.stockmarketinsights.screensUi.WatchlistScreen
import com.example.stockmarketinsights.screensUi.WatchlistSearchScreen
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.example.stockmarketinsights.viewmodel.ExploreViewModelFactory
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModelFactory
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun safeDecode(input: String?): String {
    return try {
        URLDecoder.decode(input ?: "", StandardCharsets.UTF_8.toString())
    } catch (_: Exception) {
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

    // Watchlist
    val watchlistRepository = WatchlistRepository(db.watchlistDao())
    val watchlistViewModel: WatchlistViewModel =
        viewModel(factory = WatchlistViewModelFactory(watchlistRepository))

    // Explore
    val exploreRepository = StockRepository(context = context, db = db)
    val exploreViewModel: ExploreViewModel =
        viewModel(factory = ExploreViewModelFactory(exploreRepository))

    NavHost(
        navController = navController,
        startDestination = Screen.Explore.route,
        modifier = modifier
    ) {

        // EXPLORE
        composable(Screen.Explore.route) {
            ExploreScreen(
                modifier = modifier,
                viewModel = exploreViewModel,
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
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
                onViewAllGainers = {
                    navController.navigate(Screen.ViewAll.withArgs("gainers"))
                },
                onViewAllLosers = {
                    navController.navigate(Screen.ViewAll.withArgs("losers"))
                }
            )
        }

        // VIEW ALL
        composable(
            route = Screen.ViewAll.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "gainers"
            ViewAllScreen(
                navController = navController,
                category = category,
                viewModel = exploreViewModel,
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
            val stock = StockSummaryItem(
                name = safeDecode(backStackEntry.arguments?.getString("name")),
                symbol = safeDecode(backStackEntry.arguments?.getString("symbol")),
                price = safeDecode(backStackEntry.arguments?.getString("price")),
                changePercent = safeDecode(backStackEntry.arguments?.getString("changePercent"))
            )

            DetailsScreen(stock = stock, navController = navController)
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
                    navController.navigate(Screen.WatchlistDetail.withArgs(name))
                },
                onSearchClick = {
                    navController.navigate("watchlistSearch")
                }
            )
        }

        composable("watchlistDetail/{watchlistName}") { backStackEntry ->
            val watchlistName =
                safeDecode(backStackEntry.arguments?.getString("watchlistName"))

            WatchlistDetailScreen(
                watchlistName = watchlistName,
                navController = navController,
                watchlistViewModel = watchlistViewModel,
                exploreViewModel = exploreViewModel,
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
                    navController.navigate(Screen.WatchlistDetail.withArgs(name))
                }
            )
        }

        // GLOBAL SEARCH
        composable(Screen.Search.route) {
            SearchAllStocksScreen(
                viewModel = exploreViewModel,
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