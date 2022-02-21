package com.tutv.android.ui.series

import com.tutv.android.domain.Episode

fun interface EpisodeClickListener {
    fun onClick(episode: Episode)
}