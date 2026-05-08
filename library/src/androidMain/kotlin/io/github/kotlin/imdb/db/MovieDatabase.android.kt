package io.github.kotlin.imdb.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

lateinit var applicationContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase> {
    val dbFile = applicationContext.getDatabasePath("movies.db")
    return Room.databaseBuilder<MovieDatabase>(
        context = applicationContext,
        name = dbFile.absolutePath
    )
}


