package com.tutv.android.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "review",
    foreignKeys = [ForeignKey(
        entity = Series::class,
        parentColumns = arrayOf("series_id"),
        childColumns = arrayOf("series_id")
    )]
)
data class Review   (@PrimaryKey @ColumnInfo(name = "review_id") var id: Long, @ColumnInfo(name = "series_id") val seriesId: Int, var likes: Long, var body: String, var seriesReviewComments: List<ReviewComment> = emptyList(), var user: User, var spam: Boolean)