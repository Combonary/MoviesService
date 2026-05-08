package io.github.kotlin.imdb.model

import kotlinx.serialization.Serializable

@Serializable
data class TrendingMovies(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)
