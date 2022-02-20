package com.tutv.android.domain

data class ReviewComment(val id: Long, val body: String, val likes: Long, val spam: Boolean)
