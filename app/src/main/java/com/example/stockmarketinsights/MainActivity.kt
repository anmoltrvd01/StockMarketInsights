package com.example.stockmarketinsights

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.stockmarketinsights.roomdb.AppDatabase
import com.example.stockmarketinsights.roomdb.WatchlistRepository
import com.example.stockmarketinsights.viewmodel.ExploreViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel
import com.example.stockmarketinsights.viewmodel.WatchlistViewModelFactory
import com.example.stockmarketinsights.screensUi.MainScreen

class MainActivity : ComponentActivity() {

    private lateinit var watchlistViewModel: WatchlistViewModel
    private lateinit var exploreViewModel: ExploreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create Room database instance
        val database = AppDatabase.getDatabase(this)
        val repository = WatchlistRepository(database.watchlistDao())

        // Setup ViewModels
        watchlistViewModel = ViewModelProvider(
            this,
            WatchlistViewModelFactory(repository)
        )[WatchlistViewModel::class.java]

        exploreViewModel = ViewModelProvider(this)[ExploreViewModel::class.java]

        setContent {
            MainScreen(
                exploreViewModel = exploreViewModel,
                watchlistViewModel = watchlistViewModel
            )
        }
    }
}
