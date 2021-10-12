package com.glebkrep.yandexcup.yoga.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glebkrep.yandexcup.yoga.data.BreathingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BreathingItemDao {
    @Query("select * from breathing_item_table order by start_time asc")
    suspend fun getAllItems(): Flow<BreathingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(breathingItem: BreathingItem): Long

    @Query("delete from breathing_item_table where id=:id")
    suspend fun deleteById(id: Int)

    @Query("delete from breathing_item_table")
    suspend fun clearAll()
}
