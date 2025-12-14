package com.shubhamjitseekho.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeListResponseDto(
    @SerializedName("data")
    val data: List<AnimeDto>,
    
    @SerializedName("pagination")
    val pagination: PaginationDto
)

data class AnimeDto(
    @SerializedName("mal_id")
    val malId: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("images")
    val images: ImagesDto,
    
    @SerializedName("episodes")
    val episodes: Int?,
    
    @SerializedName("score")
    val score: Double?
)

data class PaginationDto(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,
    
    @SerializedName("has_next_page")
    val hasNextPage: Boolean
)

data class ImagesDto(
    @SerializedName("jpg")
    val jpg: JpgImageDto
)

data class JpgImageDto(
    @SerializedName("image_url")
    val imageUrl: String,
    
    @SerializedName("small_image_url")
    val smallImageUrl: String,
    
    @SerializedName("large_image_url")
    val largeImageUrl: String
)