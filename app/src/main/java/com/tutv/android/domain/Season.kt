package com.tutv.android.domain

import androidx.room.*
import com.tutv.android.domain.Series
import com.tutv.android.domain.Episode

@Entity(
    tableName = "season",
    foreignKeys = [ForeignKey(
        entity = Series::class,
        parentColumns = arrayOf("series_id"),
        childColumns = arrayOf("series_id")
    )]
)
class Season {
    @PrimaryKey
    @ColumnInfo(name = "season_id")
    var id = 0
    var number = 0

    @Ignore
    var episodes: List<Episode>? = null

    @Ignore
    var isExpanded = false

    @ColumnInfo(name = "series_id")
    var seriesId = 0
}