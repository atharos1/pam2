package com.tutv.android.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tutv.android.domain.Series
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

object SeriesListToStringConverter {
    var gson: Gson? = Gson()

    @TypeConverter
    fun stringToSeriesList(data: String?): MutableList<Series?>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<MutableList<Series?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun seriesListToString(seriesList: MutableList<Series?>?): String? {
        return gson.toJson(seriesList)
    }
}