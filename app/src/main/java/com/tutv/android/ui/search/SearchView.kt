package com.tutv.android.ui.search

import com.tutv.android.domain.Genre
import com.tutv.android.domain.Network
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface SearchView {
    open fun setSearchQuery(searchQuery: String?, genre: Int?, network: Int?)
    open fun setFilters(genres: MutableList<Genre?>?, networks: MutableList<Network?>?)
    open fun showFilterError()
}