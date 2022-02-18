package com.tutv.android.ui.series

import com.tutv.android.domain.Episode
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface EpisodeClickListener {
    open fun onClick(episode: Episode?)
}