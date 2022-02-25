package com.tutv.android.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "review_comment",
    foreignKeys = [ForeignKey(
        entity = Review::class,
        parentColumns = arrayOf("review_id"),
        childColumns = arrayOf("review_id")
    )]
)
data class ReviewComment(@PrimaryKey @ColumnInfo(name = "review_comment_id") val id: Long, @ColumnInfo(name = "review_id") val review_id: Int, val body: String, val likes: Long, val spam: Boolean)
