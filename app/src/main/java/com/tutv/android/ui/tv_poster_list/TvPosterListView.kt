package com.tutv.android.ui.tv_poster_list

interface TvPosterListView {
    fun setLoadingStatus(status: Boolean)
    fun setListName(listName: String?)
    fun notifyEndReached()
    fun showLoadError()
}