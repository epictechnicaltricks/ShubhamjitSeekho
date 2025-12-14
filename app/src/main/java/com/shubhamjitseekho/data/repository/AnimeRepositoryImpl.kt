package com.shubhamjitseekho.data.repository

import android.util.Log
import com.shubhamjitseekho.data.local.dao.AnimeDao
import com.shubhamjitseekho.data.local.entity.AnimeEntity
import com.shubhamjitseekho.data.remote.JikanApi
import com.shubhamjitseekho.domain.model.Anime
import com.shubhamjitseekho.domain.model.AnimeDetail
import com.shubhamjitseekho.domain.repository.AnimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AnimeRepositoryImpl(
    private val api: JikanApi,
    private val dao: AnimeDao
) : AnimeRepository {
    
    companion object {
        private const val TAG = "AnimeRepository"
        private const val CACHE_TIMEOUT = 3600000L
    }
    
    override fun getTopAnime(): Flow<List<Anime>> = flow {
        dao.getAllAnime()
            .map { entities -> entities.map { it.toAnime() } }
            .collect { cachedAnime ->
                emit(cachedAnime)
                
                if (shouldRefreshCache(cachedAnime.isEmpty())) {
                    fetchAndCacheTopAnime()
                }
            }
    }.flowOn(Dispatchers.IO)
    
    override fun getAnimeDetail(id: Int): Flow<AnimeDetail?> = flow {
        dao.getAnimeById(id)
            .map { entity -> entity?.toAnimeDetail() }
            .collect { cachedDetail ->
                if(cachedDetail!=null){
                    emit(cachedDetail)
                }


                if (cachedDetail == null || shouldRefreshDetail(id)) {
                    fetchAndCacheAnimeDetail(id)
                }
            }
    }.flowOn(Dispatchers.IO)
    
    override suspend fun refreshAnimeList(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Force refreshing anime list...")
            dao.clearAll()
            fetchAndCacheTopAnime()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing anime list", e)
            Result.failure(e)
        }
    }
    
    private suspend fun fetchAndCacheTopAnime() {
        try {
            Log.d(TAG, "Fetching top anime from API...")
            
            val response = api.getTopAnime(page = 1, limit = 25)
            
            val entities = response.data.map { dto ->
                AnimeEntity.fromListDto(
                    id = dto.malId,
                    title = dto.title,
                    imageUrl = dto.images.jpg.largeImageUrl,
                    episodes = dto.episodes,
                    score = dto.score
                )
            }
            
            dao.insertAnimeList(entities)
            Log.d(TAG, "Cached ${entities.size} anime")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching top anime", e)
        }
    }
    
    private suspend fun fetchAndCacheAnimeDetail(id: Int) {
        try {
            Log.d(TAG, "Fetching anime detail for ID: $id")

            val response = api.getAnimeDetail(id)
            val dto = response.data

            val entity = AnimeEntity.fromDetailDto(
                id = dto.malId,
                title = dto.title,
                imageUrl = dto.images.jpg.largeImageUrl,
                synopsis = dto.synopsis ?: "No synopsis available",
                genres = dto.genres.map { it.name },
                episodes = dto.episodes,
                score = dto.score,
                status = dto.status,
                trailerUrl = dto.trailer?.embed_url
            )

            dao.insertAnime(entity)
            Log.d(TAG, "Cached anime detail for: ${dto.title}")

        } catch (e: Exception) {
            Log.e(TAG, "Error fetching anime detail", e)
        }
    }

    private fun shouldRefreshCache(isEmpty: Boolean): Boolean {
        if (isEmpty) return true
        return false
    }
    
    private suspend fun shouldRefreshDetail(id: Int): Boolean {
        val isFetched = dao.isDetailFetched(id) ?: false
        return !isFetched
    }
}