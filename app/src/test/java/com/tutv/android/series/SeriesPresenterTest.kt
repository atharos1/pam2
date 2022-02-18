package com.tutv.android.series

import com.tutv.android.domain.Episode
import com.tutv.android.domain.Season
import com.tutv.android.domain.Series
import com.tutv.android.repository.SeriesRepository
import com.tutv.android.ui.series.SeriesPresenter
import com.tutv.android.ui.series.SeriesView
import com.tutv.android.utils.schedulers.BaseSchedulerProvider
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.util.*

class SeriesPresenterTest {
    private var view: SeriesView? = null
    private var seriesRepository: SeriesRepository? = null
    private val seriesId = 1
    private val seriesName: String? = "Series"
    private val seriesDescription: String? = "Description"
    private val seriesLoggedInUserFollows = false
    private val seriesFollowers = 10
    private val seriesBannerUrl: String? = "http://banner.com"
    private var seriesSeasons: MutableList<Season?>? = null
    private var season: Season? = null
    private var episode: Episode? = null
    private var episodes: MutableList<Episode?>? = null
    private var series: Series? = null
    private var presenter: SeriesPresenter? = null

    @Before
    fun setup() {
        view = Mockito.mock(SeriesView::class.java)
        seriesRepository = Mockito.mock(SeriesRepository::class.java)
        val schedulerProvider: BaseSchedulerProvider = ImmediateSchedulerProvider()
        season = Season()
        episode = Episode()
        episodes = ArrayList()
        episodes.add(episode)
        season.setEpisodes(episodes)
        seriesSeasons = ArrayList()
        seriesSeasons.add(season)
        series = Series()
        series.setId(seriesId)
        series.setName(seriesName)
        series.setSeriesDescription(seriesDescription)
        series.setFollowers(seriesFollowers)
        series.setLoggedInUserFollows(seriesLoggedInUserFollows)
        series.setSeasons(seriesSeasons)
        series.setBannerUrl(seriesBannerUrl)
        presenter = SeriesPresenter(view, seriesId, seriesRepository, schedulerProvider)
    }

    @Test
    fun givenTheViewWasAttechedThenShowSeries() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series))
        presenter.onViewAttached()
        Mockito.verify(seriesRepository, Mockito.timeout(1000).times(1)).getSeriesById(seriesId)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showSeriesName(seriesName)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showSeriesDescription(seriesDescription)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showSeriesFollowed(seriesLoggedInUserFollows)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showFollowerCount(seriesFollowers)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).bindSeasons(seriesSeasons)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showSeriesBanner(seriesBannerUrl)
    }

    @Test
    fun givenTheViewWasAttechedWhenNetworkErrorThenShowError() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.error(Throwable()))
        presenter.onViewAttached()
        Mockito.verify(seriesRepository, Mockito.timeout(1000).times(1)).getSeriesById(seriesId)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showError(ArgumentMatchers.any())
    }

    @Test
    fun givenAnEpisodeWasClickedThenSetEpisodeAsViewed() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series))
        Mockito.`when`(seriesRepository.setEpisodeViewed(series, season, episode)).thenReturn(Single.just(series))
        presenter.onViewAttached()
        presenter.onEpisodeClicked(season, episode)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).bindSeason(season)
    }

    @Test
    fun givenAnEpisodeWasClickedWhenNetworkErrorThenShowError() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series))
        Mockito.`when`(seriesRepository.setEpisodeViewed(series, season, episode)).thenReturn(Single.error(Throwable()))
        presenter.onViewAttached()
        presenter.onEpisodeClicked(season, episode)
        Mockito.verify(view, Mockito.never()).bindSeason(ArgumentMatchers.any())
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showError(ArgumentMatchers.any())
    }

    @Test
    fun givenASeriesWasFollowedWhenNotFollowingThenSetItAsFollowed() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series))
        Mockito.`when`(seriesRepository.setFollowSeries(series)).thenReturn(Single.just(series))
        presenter.onViewAttached()
        presenter.onSeriesFollowClicked()
        Mockito.verify(seriesRepository, Mockito.timeout(1000).times(1)).setFollowSeries(series)
        Mockito.verify(view, Mockito.timeout(1000).times(2)).showFollowerCount(seriesFollowers)
        Mockito.verify(view, Mockito.timeout(1000).times(2)).showSeriesFollowed(seriesLoggedInUserFollows)
    }

    @Test
    fun givenASeriesWasFollowedWhenNotFollowingAndNetworkErrorThenShowError() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series))
        Mockito.`when`(seriesRepository.setFollowSeries(series)).thenReturn(Single.error(Throwable()))
        presenter.onViewAttached()
        presenter.onSeriesFollowClicked()
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showError(ArgumentMatchers.any())
    }

    @Test
    fun givenASeriesWasFollowedWhenFollowingThenSetItAsUnfollowed() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series))
        Mockito.`when`(seriesRepository.unfollowSeries(series)).thenReturn(Single.just(series))
        series.setLoggedInUserFollows(true)
        presenter.onViewAttached()
        presenter.onSeriesFollowClicked()
        Mockito.verify(seriesRepository, Mockito.timeout(1000).times(1)).unfollowSeries(series)
        Mockito.verify(view, Mockito.timeout(1000).times(2)).showFollowerCount(seriesFollowers)
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showSeriesFollowed(seriesLoggedInUserFollows)
    }

    @Test
    fun givenASeriesWasFollowedWhenFollowingAndNetworkErrorThenShowError() {
        Mockito.`when`(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series))
        Mockito.`when`(seriesRepository.unfollowSeries(series)).thenReturn(Single.error(Throwable()))
        series.setLoggedInUserFollows(true)
        presenter.onViewAttached()
        presenter.onSeriesFollowClicked()
        Mockito.verify(view, Mockito.timeout(1000).times(1)).showError(ArgumentMatchers.any())
    }
}