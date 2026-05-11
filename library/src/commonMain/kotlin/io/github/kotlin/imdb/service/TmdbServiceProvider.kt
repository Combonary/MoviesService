package io.github.kotlin.imdb.service

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.kotlin.imdb.utils.TmdbException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

fun provideTmdbService(
    baseUrl: String = "https://api.themoviedb.org/3/",
    httpClient: HttpClient? = null,
    token: String
): TmdbService {
    val client = if (httpClient != null) {
        // Wrap the provided client with token support
        httpClient.config {
            defaultRequest {
                header("Authorization", "Bearer $token")
            }
        }
    } else {
        HttpClient {
            install(Logging) {
                level = LogLevel.INFO
                logger = Logger.DEFAULT
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 10000
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                )
            }
            defaultRequest {
                header("Authorization", "Bearer $token")
            }
            HttpResponseValidator {
                validateResponse { response ->
                    when (val statusCode = response.status.value) {
                        401 -> throw TmdbException.UnauthorizedException
                        404 -> throw TmdbException.NotFoundException
                        in 400..499 -> throw TmdbException.UnknownApiException(statusCode)
                        in 500..599 -> throw TmdbException.UnknownApiException(statusCode)
                    }
                }

                handleResponseExceptionWithRequest { cause, _ ->
                    // You can also catch IOExceptions here for network failures
                    if (cause is IOException) {
                        throw TmdbException.NetworkException
                    }
                }
            }
        }
    }

    val ktorfit = Ktorfit.Builder()
        .baseUrl(baseUrl)
        .httpClient(client)
        .build()

    return ktorfit.createTmdbService()
}
