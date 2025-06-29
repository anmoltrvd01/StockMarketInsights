package com.example.stockmarketinsights.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GainerDao {
    @Query("SELECT * FROM gainers")
    fun getAll(): Flow<List<GainerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gainers: List<GainerEntity>)

    @Query("DELETE FROM gainers")
    suspend fun clearAll()
}
