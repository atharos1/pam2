package com.tutv.android.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tutv.android.domain.Series

object SeriesListToStringConverter {
    var gson = Gson()
    @TypeConverter
    fun stringToSeriesList(data: String): List<Series> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Series?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun seriesListToString(seriesList: List<Series?>?): String {
        return gson.toJson(seriesList)
    }
}