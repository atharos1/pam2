package com.tutv.android.profile;

import com.tutv.android.domain.User;
import com.tutv.android.repository.UserRepository;
import com.tutv.android.ui.profile.ProfilePresenter;
import com.tutv.android.ui.profile.ProfileView;
import com.tutv.android.utils.schedulers.BaseSchedulerProvider;
import com.tutv.android.utils.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfilePresenterTest {
    private ProfileView view;
    private UserRepository userRepository;

    private User user;
    private final String userMail = "example@mail.com";
    private final String userUserName = "tutvuser";

    private ProfilePresenter presenter;

    @Before
    public void setup() {
        view = mock(ProfileView.class);
        userRepository = mock(UserRepository.class);
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        user = new User();
        user.setMail(userMail);
        user.setUserName(userUserName);

        presenter = new ProfilePresenter(view, userRepository, schedulerProvider);
    }

    @Test
    public void givenTheViewWasAttachedThenLoadSuccessful() {
        when(userRepository.getCurrentUser(false)).thenReturn(Single.just(user));

        presenter.onViewAttached();

        verify(view, timeout(1000).times(1)).setLoading(false);
        verify(view, timeout(1000).times(1)).setMail(userMail);
        verify(view, timeout(1000).times(1)).setUsername(userUserName);
    }

    @Test
    public void givenTheViewWasAttachedWhenNetworkErrorThenShowError() {
        when(userRepository.getCurrentUser(false)).thenReturn(Single.error(new Throwable()));

        presenter.onViewAttached();

        verify(view, timeout(1000).times(1)).setLoading(false);
        verify(view, timeout(1000).times(1)).setLayout(false);
    }

}
