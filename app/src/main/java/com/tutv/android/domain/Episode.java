package com.tutv.android.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Season.class, parentColumns = "season_id", childColumns = "season_id")})
public class Episode {

    @PrimaryKey
    @ColumnInfo(name = "episode_id")
    private int id;

    private String name;
    @ColumnInfo(name = "num_episode") private int numEpisode;

    @ColumnInfo(name = "season_id")
    private int seasonId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumEpisode() {
        return numEpisode;
    }

    public void setNumEpisode(int numEpisode) {
        this.numEpisode = numEpisode;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }
}
