package io.github.kotlin.imdb.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieDescription(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val genres: List<Genres>
)
