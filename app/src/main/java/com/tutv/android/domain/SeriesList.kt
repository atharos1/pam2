package com.tutv.android.domain

import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tutv.android.db.converter.SeriesListToStringConverter

class SeriesList {
    @PrimaryKey val id: String? = null
    var name: String? = null
    @TypeConverters(SeriesListToStringConverter::class) var list: MutableList<Series?>? = null
    var lastLoadedPage = 0
}