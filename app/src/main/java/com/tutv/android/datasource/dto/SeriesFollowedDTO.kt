package com.tutv.android.datasource.dto

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class SeriesFollowedDTO {
    private var seriesId = 0

    constructor() {}
    constructor(seriesId: Int) {
        this.seriesId = seriesId
    }

    fun getSeriesId(): Int {
        return seriesId
    }

    fun setSeriesId(seriesId: Int) {
        this.seriesId = seriesId
    }
}