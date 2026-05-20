package io.github.kotlin.imdb.repository

import io.github.kotlin.imdb.db.MovieDao
import io.github.kotlin.imdb.model.MovieDto
import io.github.kotlin.imdb.model.MovieEntity
import io.github.kotlin.imdb.model.TrendingMovies
import io.github.kotlin.imdb.service.TmdbService
import io.github.kotlin.imdb.model.MovieDescription
import io.github.kotlin.imdb.model.Genres
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeTmdbService : TmdbService {
    var moviesToReturn = TrendingMovies(1, emptyList(), 1, 0)
    var movieToReturn: MovieDescription? = null
    
    override suspend fun getPopularMovies(): TrendingMovies = moviesToReturn

    override suspend fun getMovie(id: Int): MovieDescription {
        return movieToReturn ?: throw Exception("Movie not found")
    }
}

class FakeMovieDao : MovieDao {
    private val _movies = MutableStateFlow<List<MovieEntity>>(emptyList())
    var insertedMovies = emptyList<MovieEntity>()

    override suspend fun insertMovies(movies: List<MovieEntity>) {
        insertedMovies = movies
        _movies.value = movies
    }

    override fun getAllMovies(): Flow<List<MovieEntity>> = _movies

    override suspend fun clearAll() {
        _movies.value = emptyList()
    }
}

class MoviesRepositoryTest {

    @Test
    fun refreshMoviesShouldFetchMoviesFromNetworkAndSaveToDao() = runTest {
        val fakeTmdbService = FakeTmdbService()
        val fakeMovieDao = FakeMovieDao()
        val repository = MoviesRepository(fakeTmdbService, fakeMovieDao)

        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            overview = "Overview",
            posterPath = "/path",
            backdropPath = "/backdrop",
            voteAverage = 8.0,
            releaseDate = "2024-01-01",
            popularity = 10.0,
            genreIds = listOf(1),
            adult = false,
            voteCount = 100,
            originalLanguage = "en",
            originalTitle = "Test Movie",
            video = false
        )
        fakeTmdbService.moviesToReturn = TrendingMovies(1, listOf(movieDto), 1, 1)

        repository.refreshMovies()

        assertEquals(1, fakeMovieDao.insertedMovies.size)
        assertEquals("Test Movie", fakeMovieDao.insertedMovies[0].title)
    }

    @Test
    fun getMovieByIdShouldFetchFromNetwork() = runTest {
        val fakeTmdbService = FakeTmdbService()
        val fakeMovieDao = FakeMovieDao()
        val repository = MoviesRepository(fakeTmdbService, fakeMovieDao)

        val expectedMovie = MovieDescription(
            id = 1,
            title = "Test Movie",
            overview = "Overview",
            posterPath = "/path",
            genres = listOf(Genres(1, "Action"))
        )
        fakeTmdbService.movieToReturn = expectedMovie

        val result = repository.getMovieById(1)

        assertEquals(expectedMovie, result)
    }
}
