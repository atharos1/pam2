package com.tutv.android.domain

data class Review(var id: Long, var likes: Long, var body: String, var seriesReviewComments: List<ReviewComment> = emptyList(), var user: User, var spam: Boolean)