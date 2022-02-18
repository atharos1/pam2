package com.tutv.android.domain

import androidx.room.*

@Entity(tableName = "season",
        foreignKeys = [ForeignKey(
                entity = Series::class,
                parentColumns = ["series_id"],
                childColumns =  ["series_id"])
        ]
)
class Season {
    @PrimaryKey @ColumnInfo(name = "season_id") var id = 0
    var number = 0
    @Ignore var episodes: MutableList<Episode?>? = null
    @Ignore var isExpanded = false
    @ColumnInfo(name = "series_id") var seriesId = 0
}