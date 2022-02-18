package com.tutv.android.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "series")
class Series {
    @PrimaryKey @ColumnInfo(name = "series_id") var id = 0
    var followers = 0
    var name: String? = null
    var seriesDescription: String? = null
    var bannerUrl: String? = null
    var posterUrl: String? = null
    @Ignore var seasons: MutableList<Season?>? = null
    var loggedInUserFollows: Boolean? = null
}