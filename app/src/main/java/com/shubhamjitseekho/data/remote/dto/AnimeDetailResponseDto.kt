// 📁 app/src/main/java/com/shubhamjitseekho/data/remote/dto/AnimeDetailDto.kt
package com.shubhamjitseekho.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseDto(
    @SerializedName("data")
    val data: AnimeDetailDto
)

data class AnimeDetailDto(
    @SerializedName("mal_id")
    val malId: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("images")
    val images: ImagesDto,
    
    @SerializedName("synopsis")
    val synopsis: String?,
    
    @SerializedName("genres")
    val genres: List<GenreDto>,
    
    @SerializedName("episodes")
    val episodes: Int?,
    
    @SerializedName("score")
    val score: Double?,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("trailer")
    val trailer: TrailerDto?
)

data class GenreDto(
    @SerializedName("mal_id")
    val malId: Int,
    
    @SerializedName("name")
    val name: String
)

data class TrailerDto(
    @SerializedName("youtube_id")
    val youtubeId: String?,
    
    @SerializedName("url")
    val url: String?
) {
    fun getEmbedUrl(): String? {
        return youtubeId?.let { "https://www.youtube.com/embed/$it" }
    }
}