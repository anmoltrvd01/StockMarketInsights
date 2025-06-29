package com.example.stockmarketinsights.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LoserDao {
    @Query("SELECT * FROM losers")
    fun getAll(): Flow<List<LoserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(losers: List<LoserEntity>)

    @Query("DELETE FROM losers")
    suspend fun clearAll()
}
