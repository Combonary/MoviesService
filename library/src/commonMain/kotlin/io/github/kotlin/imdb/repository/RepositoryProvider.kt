package io.github.kotlin.imdb.repository

import io.github.kotlin.imdb.db.getDatabase
import io.github.kotlin.imdb.service.provideTmdbService

/**
 * Factory function to provide a [MoviesRepository] with its dependencies.
 */
fun provideMoviesRepository(token: String): MoviesRepository {
    val database = getDatabase()
    val service = provideTmdbService(token = token)
    return MoviesRepository(service, database.movieDao())
}
