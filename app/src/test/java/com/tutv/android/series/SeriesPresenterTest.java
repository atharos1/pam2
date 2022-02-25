package com.tutv.android.series;

import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Review;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;
import com.tutv.android.domain.User;
import com.tutv.android.repository.SeriesRepository;
import com.tutv.android.ui.series.SeriesPresenter;
import com.tutv.android.ui.series.SeriesView;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SeriesPresenterTest {
    private SeriesView view;
    private SeriesRepository seriesRepository;

    private final int seriesId = 1;
    private final String seriesName = "Series";
    private final String seriesDescription = "Description";
    private final boolean seriesLoggedInUserFollows = false;
    private final int seriesFollowers = 10;
    private final String seriesBannerUrl = "http://banner.com";
    private List<Season> seriesSeasons;
    private Season season;
    private Episode episode;
    private List<Episode> episodes;

    private final int reviewId = 1;
    private final String reviewBody = "A series review";
    private final int reviewLikes = 5;

    private Series series;
    private List<Review> reviews;

    private SeriesPresenter presenter;

    @Before
    public void setup() {
        view = mock(SeriesView.class);
        seriesRepository = mock(SeriesRepository.class);
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        season = new Season();
        episode = new Episode();
        episodes = new ArrayList<>();
        episodes.add(episode);
        season.setEpisodes(episodes);
        seriesSeasons = new ArrayList<>();
        seriesSeasons.add(season);

        series = new Series();
        series.setId(seriesId);
        series.setName(seriesName);
        series.setSeriesDescription(seriesDescription);
        series.setFollowers(seriesFollowers);
        series.setLoggedInUserFollows(seriesLoggedInUserFollows);
        series.setSeasons(seriesSeasons);
        series.setBannerUrl(seriesBannerUrl);

        reviews = new ArrayList<>();
        reviews.add(new Review(
                reviewId, seriesId, reviewLikes, reviewBody, false,
                Collections.emptyList(), new User(), false
        ));

        presenter = new SeriesPresenter(view, seriesId, seriesRepository, schedulerProvider);
    }

    @Test
    public void givenTheViewWasAttechedThenShowSeries() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));

        presenter.onViewAttached();

        verify(seriesRepository, timeout(1000).times(1)).getSeriesById(seriesId);
        verify(view, timeout(1000).times(1)).showSeriesName(seriesName);
        verify(view, timeout(1000).times(1)).showSeriesDescription(seriesDescription);
        verify(view, timeout(1000).times(1)).showSeriesFollowed(seriesLoggedInUserFollows);
        verify(view, timeout(1000).times(1)).showFollowerCount(seriesFollowers);
        verify(view, timeout(1000).times(1)).bindSeasons(seriesSeasons);
        verify(view, timeout(1000).times(1)).showSeriesBanner(seriesBannerUrl);
    }

    @Test
    public void givenTheViewWasAttechedWhenNetworkForSeriesErrorThenShowError() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.error(new Throwable()));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));

        presenter.onViewAttached();

        verify(seriesRepository, timeout(1000).times(1)).getSeriesById(seriesId);
        verify(view, timeout(1000).times(1)).showError(any());
    }

    @Test
    public void givenAnEpisodeWasClickedThenSetEpisodeAsViewed() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.setEpisodeViewed(series, season, episode)).thenReturn(Single.just(series));

        presenter.onViewAttached();
        presenter.onEpisodeClicked(season, episode);

        verify(view, timeout(1000).times(1)).bindSeason(season);
    }

    @Test
    public void givenAnEpisodeWasClickedWhenNetworkErrorThenShowError() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.setEpisodeViewed(series, season, episode)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();
        presenter.onEpisodeClicked(season, episode);

        verify(view, never()).bindSeason(any());
        verify(view, timeout(1000).times(1)).showError(any());
    }

    @Test
    public void givenASeriesWasFollowedWhenNotFollowingThenSetItAsFollowed() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.setFollowSeries(series)).thenReturn(Single.just(series));

        presenter.onViewAttached();
        presenter.onSeriesFollowClicked();

        verify(seriesRepository, timeout(1000).times(1)).setFollowSeries(series);
        verify(view, timeout(1000).times(2)).showFollowerCount(seriesFollowers);
        verify(view, timeout(1000).times(2)).showSeriesFollowed(seriesLoggedInUserFollows);
    }

    @Test
    public void givenASeriesWasFollowedWhenNotFollowingAndNetworkErrorThenShowError() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.setFollowSeries(series)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();
        presenter.onSeriesFollowClicked();

        verify(view, timeout(1000).times(1)).showError(any());
    }

    @Test
    public void givenASeriesWasFollowedWhenFollowingThenSetItAsUnfollowed() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.unfollowSeries(series)).thenReturn(Single.just(series));

        series.setLoggedInUserFollows(true);

        presenter.onViewAttached();
        presenter.onSeriesFollowClicked();

        verify(seriesRepository, timeout(1000).times(1)).unfollowSeries(series);
        verify(view, timeout(1000).times(2)).showFollowerCount(seriesFollowers);
        verify(view, timeout(1000).times(1)).showSeriesFollowed(seriesLoggedInUserFollows);
    }

    @Test
    public void givenASeriesWasFollowedWhenFollowingAndNetworkErrorThenShowError() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.unfollowSeries(series)).thenReturn(Single.error(new Throwable()));

        series.setLoggedInUserFollows(true);

        presenter.onViewAttached();
        presenter.onSeriesFollowClicked();

        verify(view, timeout(1000).times(1)).showError(any());
    }

    @Test
    public void givenTheViewWasAttechedThenShowReviews() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));

        presenter.onViewAttached();

        verify(seriesRepository, timeout(1000).times(1)).getReviews(seriesId);
        verify(view, timeout(1000).times(1)).bindReviews(reviews);
    }

    @Test
    public void givenTheViewWasAttechedWhenNetworkForReviewsErrorThenShowError() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();

        verify(seriesRepository, timeout(1000).times(1)).getReviews(seriesId);
        verify(view, timeout(1000).times(1)).showError(any());
    }

    @Test
    public void givenAReviewLikeWasClickedThenSetReviewAsLiked() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.setReviewLiked(series, reviews.get(0), true)).thenReturn(Single.just(reviews.get(0)));

        presenter.onViewAttached();
        presenter.onReviewLikeClicked(reviews.get(0));

        verify(view, timeout(1000).times(2)).bindReviews(reviews);
    }

    @Test
    public void givenAReviewLikeWasClickedWhenNetworkErrorThenShowError() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.setReviewLiked(series, reviews.get(0), true)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();
        presenter.onReviewLikeClicked(reviews.get(0));

        verify(view, timeout(1000).times(1)).bindReviews(reviews);
        verify(view, timeout(1000).times(1)).showError(any());
    }

    @Test
    public void givenReviewSubmitWasClickedThenPostReview() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.postReview(series, reviewBody)).thenReturn(Single.just(reviews.get(0)));

        presenter.onViewAttached();
        presenter.onReviewSubmitClicked(reviewBody);

        verify(seriesRepository, timeout(1000).times(1)).postReview(series, reviewBody);
        verify(view, timeout(1000).times(2)).bindReviews(any());
    }

    @Test
    public void givenReviewSubmitWasClickedWhenNetworkErrorThenShowError() {
        when(seriesRepository.getSeriesById(seriesId)).thenReturn(Single.just(series));
        when(seriesRepository.getReviews(seriesId)).thenReturn(Single.just(reviews));
        when(seriesRepository.postReview(series, reviewBody)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();
        presenter.onReviewSubmitClicked(reviewBody);

        verify(view, timeout(1000).times(1)).bindReviews(any());
        verify(view, timeout(1000).times(1)).showError(any());
    }
}