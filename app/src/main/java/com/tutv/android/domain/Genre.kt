package com.tutv.android.domain

data class Genre(var id: Int, var name: String) {
    override fun toString(): String = name
}