package com.example.stockmarketinsights.roomdb

import androidx.compose.runtime.compositionLocalOf
import com.example.stockmarketinsights.viewmodel.WatchlistViewModel

val LocalWatchlistViewModel = compositionLocalOf<WatchlistViewModel> {
    error("WatchlistViewModel not provided")
}
