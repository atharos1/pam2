package com.tutv.android.domain

class Genre(var id: Int, var name: String) {
    var seriesUri: String? = null
    var series: List<Series>? = null
    override fun toString(): String {
        return name
    }
}