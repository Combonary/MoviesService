package io.github.kotlin.imdb.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TmdbServiceTest {

    @Test
    fun testGetPopularMovies() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "page": 1,
                        "results": [
                            {
                                "id": 1,
                                "title": "Test Movie",
                                "overview": "Test Overview",
                                "release_date": "2024-01-01",
                                "vote_average": 8.5,
                                "vote_count": 100,
                                "adult": false,
                                "backdrop_path": "/path",
                                "poster_path": "/poster",
                                "original_language": "en",
                                "original_title": "Original Title",
                                "video": false,
                                "popularity": 10.0,
                                "genre_ids": [1, 2]
                            }
                        ],
                        "total_pages": 1,
                        "total_results": 1
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine)

        val service = provideTmdbService(
            baseUrl = "https://api.example.com/",
            httpClient = client,
            token = "test_token"
        )
        val popularMovies = service.getPopularMovies()

        assertEquals(1, popularMovies.results.size)
        assertEquals("Test Movie", popularMovies.results[0].title)
        assertContains(popularMovies.results[0].genreIds, 1)
    }

    @Test
    fun testAuthenticationHeader() = runTest {
        var authHeader: String? = null
        val mockEngine = MockEngine { request ->
            authHeader = request.headers[HttpHeaders.Authorization]
            respond(
                content = """{"page": 1, "results": [], "total_pages": 1, "total_results": 0}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val service = provideTmdbService(
            baseUrl = "https://api.example.com/",
            httpClient = HttpClient(mockEngine),
            token = "test_token"
        )

        service.getPopularMovies()
        assertEquals("Bearer test_token", authHeader)
    }
}
