package com.shubhamjitseekho.domain.repository

import com.shubhamjitseekho.domain.model.Anime
import com.shubhamjitseekho.domain.model.AnimeDetail
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    
    fun getTopAnime(): Flow<List<Anime>>
    
    fun getAnimeDetail(id: Int): Flow<AnimeDetail?>
    
    suspend fun refreshAnimeList(): Result<Unit>
}