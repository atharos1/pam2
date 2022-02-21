package com.tutv.android.ui.series

import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season

fun interface SeasonAndEpisodeClickedListener {
    fun onClick(s: Season, e: Episode)
}