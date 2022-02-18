package com.tutv.android.ui.home

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface HomeView {
    open fun createGenreList(genreId: Int, genreName: String?)
    open fun showError()
}