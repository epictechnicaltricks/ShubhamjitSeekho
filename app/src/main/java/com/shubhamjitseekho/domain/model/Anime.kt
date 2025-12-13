// 📁 app/src/main/java/com/shubhamjitseekho/domain/model/Anime.kt
package com.shubhamjitseekho.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val episodes: Int?,
    val score: Double?
) {
    val formattedScore: String
        get() = score?.let { "⭐ %.1f".format(it) } ?: "N/A"
    
    val formattedEpisodes: String
        get() = episodes?.let { "$it Episodes" } ?: "Unknown"
}