package io.github.kotlin.imdb.service

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideImdbService(
    baseUrl: String = "https://api.themoviedb.org",
    httpClient: HttpClient? = null
): ImdbService {
    val apiKey = "f7f4fca0ec6d3ba8ddba59ca9cbcd5a8"
    val client = httpClient ?: HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }
            )
        }
        defaultRequest {
            url {
                parameters.append("api_key", apiKey)
            }
        }
    }

    val ktorfit = Ktorfit.Builder()
        .baseUrl(baseUrl)
        .httpClient(client)
        .build()

    return ktorfit.createImdbService()
}
