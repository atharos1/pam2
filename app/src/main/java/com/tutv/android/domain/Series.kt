package com.tutv.android.domain

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.tutv.android.domain.Season

@Entity(tableName = "series")
class Series {
    @PrimaryKey
    @ColumnInfo(name = "series_id")
    var id = 0
    var followers = 0
    var name: String? = null
    var seriesDescription: String? = null
    var bannerUrl: String? = null
    var posterUrl: String? = null


    @Ignore
    var seasons: List<Season>? = null
    @Ignore
    var reviews: List<Review>? = null
    var loggedInUserFollows: Boolean? = null
}