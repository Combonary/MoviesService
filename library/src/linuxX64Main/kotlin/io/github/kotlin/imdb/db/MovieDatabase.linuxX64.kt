package io.github.kotlin.imdb.db

import androidx.room.Room
import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase> {
    return Room.databaseBuilder<MovieDatabase>(
        name = "movies.db"
    )
}
