package io.github.kotlin.imdb.service

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImdbServiceTest {

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

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val ktorfit = Ktorfit.Builder()
            .baseUrl("https://api.example.com/")
            .httpClient(client)
            .build()

        val service = ktorfit.createImdbService()
        val popularMovies = service.getPopularMovies()

        assertEquals(1, popularMovies.results.size)
        assertEquals("Test Movie", popularMovies.results[0].title)
        assertTrue(popularMovies.results[0].genre_ids.contains(1))
    }
}
