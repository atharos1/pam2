package com.tutv.android.ui.tv_poster_list

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface TvPosterListView {
    open fun setLoadingStatus(status: Boolean)
    open fun setListName(listName: String?)
    open fun notifyEndReached()
    open fun showLoadError()
}