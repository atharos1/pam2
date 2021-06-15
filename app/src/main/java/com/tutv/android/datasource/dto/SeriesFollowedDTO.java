package com.tutv.android.datasource.dto;

public class SeriesFollowedDTO {
    private int seriesId;

    public SeriesFollowedDTO() {}

    public SeriesFollowedDTO(int seriesId) {
        this.seriesId = seriesId;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }
}
