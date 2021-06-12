package com.tutv.android.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"season_id", "episode_id"},
        foreignKeys = {
            @ForeignKey(entity = Season.class, parentColumns = "season_id", childColumns = "season_id"),
            @ForeignKey(entity = Episode.class, parentColumns = "episode_id", childColumns = "episode_id")
        })
public class SeasonAndEpisodeJoin {

    @ColumnInfo(name = "season_id") public int seasonId;
    @ColumnInfo(name = "episode_id") public int episodeId;

}
