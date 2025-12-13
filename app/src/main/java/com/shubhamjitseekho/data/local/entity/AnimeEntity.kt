// 📁 app/src/main/java/com/shubhamjitseekho/data/local/entity/AnimeEntity.kt
package com.shubhamjitseekho.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shubhamjitseekho.domain.model.Anime
import com.shubhamjitseekho.domain.model.AnimeDetail

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val imageUrl: String,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String? = null,
    val genres: String? = null,
    val status: String? = null,
    val trailerUrl: String? = null,
    val lastUpdated: Long = System.currentTimeMillis(),
    val isDetailFetched: Boolean = false
) {
    fun toAnime(): Anime {
        return Anime(
            id = id,
            title = title,
            imageUrl = imageUrl,
            episodes = episodes,
            score = score
        )
    }
    
    fun toAnimeDetail(): AnimeDetail? {
        if (!isDetailFetched || synopsis == null || status == null) {
            return null
        }
        
        return AnimeDetail(
            id = id,
            title = title,
            imageUrl = imageUrl,
            synopsis = synopsis,
            genres = genres?.split(",")?.map { it.trim() } ?: emptyList(),
            episodes = episodes,
            score = score,
            status = status,
            trailerUrl = trailerUrl
        )
    }
    
    companion object {
        fun fromListDto(
            id: Int,
            title: String,
            imageUrl: String,
            episodes: Int?,
            score: Double?
        ): AnimeEntity {
            return AnimeEntity(
                id = id,
                title = title,
                imageUrl = imageUrl,
                episodes = episodes,
                score = score,
                isDetailFetched = false
            )
        }
        
        fun fromDetailDto(
            id: Int,
            title: String,
            imageUrl: String,
            synopsis: String,
            genres: List<String>,
            episodes: Int?,
            score: Double?,
            status: String,
            trailerUrl: String?
        ): AnimeEntity {
            return AnimeEntity(
                id = id,
                title = title,
                imageUrl = imageUrl,
                episodes = episodes,
                score = score,
                synopsis = synopsis,
                genres = genres.joinToString(","),
                status = status,
                trailerUrl = trailerUrl,
                isDetailFetched = true
            )
        }
    }
}