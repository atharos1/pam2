package com.tutv.android.ui.home

interface HomeView {
    fun createGenreList(genreId: Int, genreName: String?)
    fun showError()
}