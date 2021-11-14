package com.tutv.android.domain

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Season::class,
        parentColumns = arrayOf("season_id"),
        childColumns = arrayOf("season_id")
    )]
)
class Episode {
    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    var id = 0
    var name: String? = null

    @ColumnInfo(name = "num_episode")
    var numEpisode = 0

    @ColumnInfo(name = "season_id")
    var seasonId = 0
    var loggedInUserViewed: Boolean? = null
}