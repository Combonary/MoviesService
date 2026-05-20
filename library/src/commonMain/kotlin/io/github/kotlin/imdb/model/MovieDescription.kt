package io.github.kotlin.imdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDescription(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("release_date") val releaseDate: String,
    val popularity: Double,
    val genres: List<Genres>
)

fun MovieDescription.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        popularity = popularity,
        genreIds = genres.map { it.id }
    )
}
