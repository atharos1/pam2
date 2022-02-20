package com.tutv.android.ui.search

import com.tutv.android.domain.Genre
import com.tutv.android.domain.Network

interface SearchView {
    fun setSearchQuery(searchQuery: String?, genre: Int?, network: Int?)
    fun setFilters(genres: MutableList<Genre?>, networks: MutableList<Network?>)
    fun showFilterError()
}