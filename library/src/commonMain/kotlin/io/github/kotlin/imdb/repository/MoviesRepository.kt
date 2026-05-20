package io.github.kotlin.imdb.repository

import io.github.kotlin.imdb.db.MovieDao
import io.github.kotlin.imdb.model.MovieEntity
import io.github.kotlin.imdb.model.toEntity
import io.github.kotlin.imdb.service.TmdbService
import io.github.kotlin.imdb.utils.TmdbException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repository that handles the coordination between the network (TMDB API)
 * and the local database (Room).
 */
class MoviesRepository(
    private val tmdbService: TmdbService,
    private val movieDao: MovieDao
) {
    /**
     * Exposes a stream of movies from the local database.
     * This is the Single Source of Truth for the UI.
     */
    val movies: Flow<List<MovieEntity>> = movieDao.getAllMovies()

    /**
     * Attempts to refresh the movies from the network.
     * If successful, the database is updated, which will automatically 
     * trigger an emission from the [movies] Flow.
     * 
     * @throws TmdbException if the network call fails.
     */
    suspend fun refreshMovies() {
        val trendingMovies = tmdbService.getPopularMovies()
        val entities = trendingMovies.results.map { it.toEntity() }
        movieDao.insertMovies(entities)
    }

    /**
     * Convenience method to get a specific movie by ID.
     * Checks the database first, and if not found, fetches from the network
     * and saves it to the database for future use.
     */
    suspend fun getMovieById(id: Int): MovieEntity {
        return movieDao.getMovieById(id).first() ?: tmdbService.getMovie(id).toEntity().also {
            movieDao.insertMovie(it)
        }
    }

    /**
     * Inserts or updates a single movie in the local database.
     */
    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }
}
