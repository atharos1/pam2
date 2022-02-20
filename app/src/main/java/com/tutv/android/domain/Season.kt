package com.tutv.android.domain

import androidx.room.*

@Entity(
    tableName = "season",
    foreignKeys = [ForeignKey(
        entity = Series::class,
        parentColumns = arrayOf("series_id"),
        childColumns = arrayOf("series_id")
    )]
)
data class Season(@PrimaryKey @ColumnInfo(name = "season_id") var id: Int = 0,
                              @ColumnInfo(name = "series_id") var seriesId: Int = 0,
                                                      @Ignore var episodes: List<Episode>? = null,
                                                      @Ignore var isExpanded: Boolean = false,
                                                              var number: Int = 0)