package io.github.kotlin.imdb.db

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase> {
    val dbFilePath = NSHomeDirectory() + "/movies.db"
    return Room.databaseBuilder<MovieDatabase>(
        name = dbFilePath
    )
}
