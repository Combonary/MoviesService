package io.github.kotlin.imdb.service

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path
import io.github.kotlin.imdb.model.MovieDescription
import io.github.kotlin.imdb.model.TrendingMovies

interface TmdbService {
    /**
     * Fetch list of trending movies for the week
     */
    @Headers("Accept: application/json")
    @GET("trending/movie/week")
    suspend fun getPopularMovies() : TrendingMovies

    /**
     * Search for movies by id.
     * @param id movie id.
     */
    @Headers("Accept: application/json")
    @GET("movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int) : MovieDescription
}
