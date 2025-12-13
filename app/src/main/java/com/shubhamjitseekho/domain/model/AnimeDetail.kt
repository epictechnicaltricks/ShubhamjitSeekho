// 📁 app/src/main/java/com/shubhamjitseekho/domain/model/AnimeDetail.kt
package com.shubhamjitseekho.domain.model

data class AnimeDetail(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val synopsis: String,
    val genres: List<String>,
    val episodes: Int?,
    val score: Double?,
    val status: String,
    val trailerUrl: String?
) {
    val formattedScore: String
        get() = score?.let { "⭐ %.1f/10".format(it) } ?: "N/A"
    
    val formattedEpisodes: String
        get() = episodes?.let { "$it Episodes" } ?: "Unknown"
    
    val genresText: String
        get() = genres.joinToString(" • ")
    
    val hasTrailer: Boolean
        get() = !trailerUrl.isNullOrBlank()
}