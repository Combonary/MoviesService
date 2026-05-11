package io.github.kotlin.imdb.utils

sealed class TmdbException(message: String): Exception(message) {
    object NetworkException: TmdbException("No internet connection")
    object UnauthorizedException: TmdbException("Invalid API Key")
    object NotFoundException: TmdbException("Resource not found")
    data class UnknownApiException(val code: Int): TmdbException("API error: $code")
}