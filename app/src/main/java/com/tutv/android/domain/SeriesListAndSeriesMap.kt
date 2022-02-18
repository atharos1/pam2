package com.tutv.android.domain

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["series_list_id", "series_id"], tableName = "serieslists_and_series_map")
class SeriesListAndSeriesMap(
        @field:ColumnInfo(name = "series_list_id") var seriesListId: String,
        @field:ColumnInfo(name = "series_id") var seriesId: Int
)