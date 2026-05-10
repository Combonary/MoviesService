# MoviesService - Kotlin Multiplatform TMDB Library

[![official project](http://jb.gg/badges/official.svg)](https://github.com/JetBrains#jetbrains-on-github)

## What is it?

MoviesService is a [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) library that provides a type-safe interface to [The Movie Database (TMDB)](https://www.themoviedb.org/) API.

### Features
- 🎬 **Popular Movies**: Fetch trending movies
- 🔍 **Movie Search**: Search movies by title
- 📋 **Movie Details**: Get detailed information about specific movies
- 📱 **Multiplatform**: Supports Android, iOS, JVM, and Linux
- 🏗️ **Ktor Integration**: Uses Ktor for HTTP requests
- 💾 **Room Database**: Local caching with SQLite
- 🔒 **Type Safety**: Full Kotlin serialization support

### Platforms Supported
- **Android** (API 24+)
- **iOS** (11.0+)
- **JVM** (Java 11+)
- **Linux** (x64)

## Quick Start

### Add to your project

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/Combonary/MoviesService")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: "YOUR_GITHUB_USERNAME"
                password = System.getenv("GITHUB_TOKEN") ?: "YOUR_GITHUB_TOKEN"
            }
        }
    }
}

// app/build.gradle.kts
dependencies {
    implementation("io.github.combonary:library-android:1.0.2")
}
```

### Basic Usage

```kotlin
import io.github.kotlin.imdb.service.provideImdbService
import kotlinx.coroutines.*

fun main() = runBlocking {
    val imdbService = provideImdbService()

    // Get popular movies
    val popularMovies = imdbService.getPopularMovies()
    popularMovies.results.forEach { movie ->
        println("${movie.title} (${movie.releaseDate?.year})")
    }

    // Search for movies
    val searchResults = imdbService.searchMovies(query = "Inception")
    println("Found ${searchResults.totalResults} movies")

    // Get movie details
    val movieDetails = imdbService.getMovieDetails(movieId = 27205)
    println("${movieDetails.title}: ${movieDetails.overview}")
}
```

## Publishing

This library is published to **GitHub Packages** for private/team distribution.

### Publish Commands
```bash
# Publish to GitHub Packages
./gradlew publish

# Test locally first
./gradlew publishToMavenLocal
```

## Project Structure

```
library/
├── src/
│   ├── commonMain/     # Shared code (Ktorfit, serialization)
│   ├── androidMain/    # Android-specific implementations
│   ├── iosMain/        # iOS-specific implementations
│   ├── jvmMain/        # JVM-specific implementations
│   └── linuxX64Main/   # Linux-specific implementations
├── build.gradle.kts    # Build configuration
└── schemas/           # Room database schemas
```

## Architecture

- **HTTP Client**: Ktor with content negotiation
- **API Generation**: Ktorfit for type-safe API calls
- **Serialization**: Kotlinx.serialization for JSON
- **Database**: Room for local caching
- **Multiplatform**: Kotlin Multiplatform with expect/actual pattern

## Development

### Prerequisites
- JDK 17+
- Kotlin 2.3.21+
- Android Studio (for Android development)

### Build
```bash
./gradlew build
```

### Run Tests
```bash
./gradlew test
```

### IDE Support
- Open in Android Studio or IntelliJ IDEA
- Full Kotlin Multiplatform support
- iOS development requires Xcode (on macOS)

## Documentation

- [GitHub Packages Setup](GITHUB_PACKAGES_SETUP.md)
- [Publishing Options](PUBLISHING_OPTIONS.md)
- [Maven Central Setup](MAVEN_CENTRAL_SETUP.md) (alternative)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Resources

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [TMDB API Documentation](https://developers.themoviedb.org/3)
- [Ktor Documentation](https://ktor.io/)
- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [Publishing Libraries Guide](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html)
