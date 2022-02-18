package com.tutv.android.ui.series

import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface SeasonAndEpisodeClickedListener {
    open fun onClick(s: Season?, e: Episode?)
}