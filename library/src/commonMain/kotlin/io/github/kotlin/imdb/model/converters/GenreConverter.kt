package io.github.kotlin.imdb.model.converters

import androidx.room.TypeConverter

class GenreConverter {
    @TypeConverter
    fun fromIntList(idList: List<Int>?): String? {
        return idList?.joinToString(",")
    }

    @TypeConverter
    fun toIntList(str: String?): List<Int>? {
        return str?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() }
    }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(str: String?): List<String>? {
        return str?.split(",")?.filter { it.isNotEmpty() }
    }
}
