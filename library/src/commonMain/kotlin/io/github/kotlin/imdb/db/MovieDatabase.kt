package io.github.kotlin.imdb.db

import androidx.room.ConstructedBy
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.kotlin.imdb.model.MovieEntity
import io.github.kotlin.imdb.model.converters.GenreConverter
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movies")
    suspend fun clearAll()
}

@Database(entities = [MovieEntity::class], version = 1)
@TypeConverters(GenreConverter::class)
@ConstructedBy(MovieDatabaseConstructor::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

// The Room compiler generates the implementation for this class
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MovieDatabaseConstructor : RoomDatabaseConstructor<MovieDatabase> {
    override fun initialize(): MovieDatabase
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<MovieDatabase>
private lateinit var instance: MovieDatabase

fun getDatabase(): MovieDatabase {
    if (!::instance.isInitialized) {
        instance = getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    return instance
}
