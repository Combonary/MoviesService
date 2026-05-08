package io.github.kotlin.imdb.model

import kotlinx.serialization.Serializable

@Serializable
data class Genres(
    val id: Int,
    val name: String
)
