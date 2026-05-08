package io.github.kotlin.imdb.db

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "movies.db")
    return Room.databaseBuilder<MovieDatabase>(
        name = dbFile.absolutePath,
    )
}
