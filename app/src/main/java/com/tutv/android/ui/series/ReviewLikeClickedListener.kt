package com.tutv.android.ui.series

import com.tutv.android.domain.Review

fun interface ReviewLikeClickedListener {
    fun onClick(r: Review)
}