// 📁 app/src/main/java/com/shubhamjitseekho/data/local/dao/AnimeDao.kt
package com.shubhamjitseekho.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shubhamjitseekho.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    
    @Query("SELECT * FROM anime ORDER BY score DESC")
    fun getAllAnime(): Flow<List<AnimeEntity>>
    
    @Query("SELECT * FROM anime WHERE id = :id")
    fun getAnimeById(id: Int): Flow<AnimeEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)
    
    @Update
    suspend fun updateAnime(anime: AnimeEntity)
    
    @Query("DELETE FROM anime")
    suspend fun clearAll()
    
    @Query("DELETE FROM anime WHERE lastUpdated < :timestamp")
    suspend fun deleteOldEntries(timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM anime")
    suspend fun getAnimeCount(): Int
    
    @Query("SELECT isDetailFetched FROM anime WHERE id = :id")
    suspend fun isDetailFetched(id: Int): Boolean?
}